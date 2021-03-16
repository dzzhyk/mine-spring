package com.yankaizhang.spring.webmvc.servlet;

import com.yankaizhang.spring.aop.support.AopUtils;
import com.yankaizhang.spring.beans.holder.BeanWrapper;
import com.yankaizhang.spring.context.annotation.Bean;
import com.yankaizhang.spring.context.impl.AnnotationConfigApplicationContext;
import com.yankaizhang.spring.context.annotation.Controller;
import com.yankaizhang.spring.web.ViewResolver;
import com.yankaizhang.spring.web.method.HandlerMethod;
import com.yankaizhang.spring.web.model.ModelAndView;
import com.yankaizhang.spring.web.view.View;
import com.yankaizhang.spring.webmvc.*;
import com.yankaizhang.spring.webmvc.annotation.RequestMapping;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;
import com.yankaizhang.spring.webmvc.multipart.MultipartResolver;
import com.yankaizhang.spring.webmvc.multipart.commons.CommonsMultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * DispatcherServlet实现
 * @author dzzhyk
 * @since 2020-11-28 13:40:18
 */
@SuppressWarnings("all")
@WebServlet(
        name = "dispatcherServlet",
        displayName = "com.yankaizhang.springframework.webmvc.servlet.DispatcherServlet",
        urlPatterns="/",
        loadOnStartup = -1,
        initParams = {
                @WebInitParam(name = "contextConfigLocation", value = "classpath:application.properties")
        }
)
public class DispatcherServlet extends FrameworkServlet {

    public static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    /** 默认文件上传解析器beanName */
    public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

    /** 默认视图解析器beanName */
    public static final String VIEW_RESOLVER_BEAN_NAME = "internalResourceViewResolver";

    /** 视图处理器的beanName */
    public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";

    /** 视图处理器适配器的beanName */
    public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";

    /**
     * 文件请求解析器
     */
    private MultipartResolver multipartResolver;
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new HashMap<>();
    private List<ViewResolver> viewResolvers = new ArrayList<>();

    private AnnotationConfigApplicationContext context;


    public DispatcherServlet() {
        super();
    }

    @Override
    public void init(ServletConfig config) {
        super.init(config);
        this.context = super.getContext();
        initStrategies(context);
    }

    private void initStrategies(AnnotationConfigApplicationContext context){
        log.debug("********** Dispatcher Servlet 初始化开始 **********");
        initMultipartResolver(context);             // 多部分文件上传解析multipart

        initLocaleResolver(context);                // 本地化解析
        initThemeResolver(context);                 // 主题解析

        initHandlerMappings(context);               // url映射到controller
        initHandlerAdapters(context);               // 多类型参数动态匹配，获得ModelAndView对象

        initHandlerExceptionResolvers(context);     // 运行异常处理
        initRequestToViewNameTranslator(context);   // 直接将请求解析到视图名
        initViewResolvers(context);                 // 通过viewResolver将逻辑视图解析为具体视图实现
        initFlashMapManager(context);               // 初始化Flash映射管理器

        log.debug("********** Dispatcher Servlet 初始化完成 **********");
    }


    /**
     * 初始化文件解析器
     */
    private void initMultipartResolver(AnnotationConfigApplicationContext context){
        MultipartResolver multipartResolver = null;
        try {
            // 尝试从容器中获取
            multipartResolver = (MultipartResolver) context.getBean(MULTIPART_RESOLVER_BEAN_NAME);
        }catch (Exception e){
            this.multipartResolver = new CommonsMultipartResolver();
            log.warn("容器中未配置 [MultipartResolver] 对象，已创建默认实现对象 : CommonsMultipartResolver");
        }
        if (multipartResolver != null){
            this.multipartResolver = multipartResolver;
            log.debug("获取了已配置 [MultipartResolver] 对象 : " + multipartResolver.getClass());
        }
    }

    /*
      这些暂时不实现
     */
    private void initFlashMapManager(AnnotationConfigApplicationContext context){}
    private void initRequestToViewNameTranslator(AnnotationConfigApplicationContext context){}
    private void initHandlerExceptionResolvers(AnnotationConfigApplicationContext context){}
    private void initThemeResolver(AnnotationConfigApplicationContext context){}
    private void initLocaleResolver(AnnotationConfigApplicationContext context){}


    /**
     * 初始化HandlerMapping
     */
    private void initHandlerMappings(AnnotationConfigApplicationContext context){
        try {
            // 获取已经存在的实例化好的对象
            Map<String, Object> ioc = context.getBeanFactory().getSingletonIoc();
            for (Object beanInstance : ioc.values()) {
                // 排除可能有的bean没有在容器中
                if (beanInstance == null) continue;
                Class<?> clazz = null;
                // 如果是Aop代理bean对象
                if (AopUtils.isAopProxy(beanInstance)){
                    // 如果是代理对象，需要获取到代理对象的最终目标类
                    clazz = AopUtils.getAopTarget(beanInstance);
                }else{
                    clazz = beanInstance.getClass();
                }

                if (clazz==null || !clazz.isAnnotationPresent(Controller.class)) continue;

                String[] baseUrls = {};
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrls = requestMapping.value();
                }

                // 获得了controller对象之后，就把方法包装成HandlerMethod对象吧
                // 将controller标注@RequestMapping的方法加入handlerMapping
                for (Method method : clazz.getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) continue;

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                    String[] methodMappings = requestMapping.value();

                    // 这里生成的最终url应该是正则表达式形式
                    // 允许同一个controller对应多个mapping
                    if (baseUrls != null && baseUrls.length != 0) {
                        // 如果存在Controller类的对应
                        for (String baseUrl : baseUrls) {
                            for (String methodMapping : methodMappings) {
                                String url = null;
                                // 特殊处理一下无后缀"/"的情况
                                if ("".equals(methodMapping.trim())){
                                    url = (baseUrl).replaceAll("/+", "/");
                                }else{
                                    url = (baseUrl + "/" + methodMapping).replaceAll("/+", "/");
                                }
                                Pattern pattern = Pattern.compile(url);
                                handlerMappings.add(
                                        new HandlerMapping(beanInstance, new HandlerMethod(beanInstance, method), pattern)
                                );
                            }
                        }
                    }else{
                        for (String methodMapping : methodMappings) {
                            // 如果没有controller根路径，则空路径情况需要避免
                            if (!"".equals(methodMapping.trim())){
                                String url = ("/" + methodMapping).replaceAll("/+", "/");
                                Pattern pattern = Pattern.compile(url);
                                handlerMappings.add(
                                        new HandlerMapping(beanInstance, new HandlerMethod(beanInstance, method), pattern)
                                );
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 注册每个handler的参数适配器
     * 注意HandlerMapping是所说的handler的包装类
     * 在参数适配器中进行传入参数和传出参数处理
     */
    private void initHandlerAdapters(AnnotationConfigApplicationContext context){
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerAdapterMap.put(handlerMapping, new HandlerAdapter());
            log.debug(String.format("映射路径 : [ %s ]", handlerMapping.getPattern()));
        }
    }


    /**
     * 注册模板解析器
     */
    private void initViewResolvers(AnnotationConfigApplicationContext context){
        // 尝试从容器中获取视图解析器
        ViewResolver internalViewResolver = null;
        try {
            internalViewResolver = (ViewResolver) context.getBean(VIEW_RESOLVER_BEAN_NAME);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (internalViewResolver != null){
            viewResolvers.add(internalViewResolver);
            log.debug("获取了已配置 [internalResourceViewResolver] 对象 : " + internalViewResolver.toString());
        }else{
            ViewResolver resolver = new ViewResolver();
            log.warn("未配置 [internalResourceViewResolver] 将使用默认viewResolver : " + resolver.toString());
            viewResolvers.add(resolver);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        }catch (Exception e){
            // 目前只是简单的处理
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("text/html;charset=utf-8");
            StringBuffer buffer = new StringBuffer();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            buffer.append("<h1>500 Exception</h1>").append(now.format(dtf))
                    .append("<br>https://github.com/dzzhyk/mine-spring<br>")
                    .append("<br>mine-spring 0.0.2<br>");
            buffer.append("<h2>Error Message</h2>").append(e.getMessage()).append("<hr><h2>StackTrace</h2>");
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                buffer.append(stackTraceElement).append("\n");
            }
            resp.getWriter().write(buffer.toString());
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String requestURI = req.getRequestURI();
        log.debug(String.format("路径请求 : [ %s ]", requestURI));

        HttpServletRequest processedRequest = req;
        boolean multipartRequestParsed = false;
        processedRequest = checkMultipart(req);

        // 如果两次解析出来的请求不是一个，说明是文件上传请求
        multipartRequestParsed = (processedRequest != req);

        HandlerMapping handlerMapping = getHandlerMapping(processedRequest);
        if (null == handlerMapping){
            // 如果没有这个controller，返回404页面
            log.warn("没有对应的 HandlerMapping => \"{}\", 尝试寻找路径为该URI的静态资源", requestURI);

            // 可能是静态资源，尝试发送到defaultDispatcher
            RequestDispatcher defaultDispatcher = getServletContext().getNamedDispatcher("default");
            defaultDispatcher.forward(req, resp);
        }

        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        if (handlerAdapter == null){
            throw new Exception("没有对应的 HandlerAdapter 实现 => \"" + requestURI + "\"");
        }

        ModelAndView mv = handlerAdapter.handle(processedRequest, resp, handlerMapping);

        // 将request中的attribute加入model
        parseRequestAttributes(mv, req);

        // 处理dispatcher结果，渲染视图
        processDispatchResult(req, resp, mv);

        // 清理上传产生的资源文件
        if (multipartRequestParsed) {
            if (this.multipartResolver != null){
                this.multipartResolver.cleanupMultipart((MultipartRequest) processedRequest);
            }
        }
    }


    /**
     * 解析request中的attribute，将其加入model
     */
    private ModelAndView parseRequestAttributes(ModelAndView modelAndView, HttpServletRequest request){
        if (modelAndView==null){
            return null;
        }
        Enumeration attributeNames = request.getAttributeNames();
        Map<String, Object> model = (Map<String, Object>) modelAndView.getModel();
        if (null == model){
            model = new HashMap<>();
        }
        while (attributeNames.hasMoreElements()){
            String s = (String) attributeNames.nextElement();
            Object attribute = request.getAttribute(s);
            model.put(s, attribute);
        }
        modelAndView.setModel(model);
        return modelAndView;
    }

    /**
     * 检查请求是否为文件上传请求
     * @return 解析后的请求
     * @throws 异常
     */
    protected HttpServletRequest checkMultipart(HttpServletRequest request) throws Exception {
        if (this.multipartResolver != null && this.multipartResolver.isMultipart(request)) {
            try {
                return this.multipartResolver.resolveMultipart(request);
            }
            catch (Exception ex) {
                if (request.getAttribute("javax.servlet.error.exception") != null) {
                    log.debug("上传文件解析失败", ex);
                    // Keep processing error dispatch with regular request handle below
                }
                else {
                    throw ex;
                }
            }
        }
        // 默认返回原请求
        return request;
    }

    /**
     * 调用视图处理器处理相应视图
     */
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav)
            throws Exception {
        if (null == mav) return;
        if (viewResolvers.isEmpty()) return;

        // 如果mav需要渲染就进行渲染
        if (!mav.isCleared()){
            try {
                render(req, resp, mav);
                mav.setCleared(true);
            }catch (Exception e){
                log.warn("渲染视图发生错误 : " + mav);
                e.printStackTrace();
            }
        }
    }

    /**
     * 渲染{@link ModelAndView}视图
     */
    private void render(HttpServletRequest req, HttpServletResponse resp, ModelAndView mav) throws Exception {

        View view = null;
        String viewName = mav.getViewName();
        if (null != viewName){
            view = resolveViewName(viewName);
            if (view == null){
                throw new Exception("找不到对应名称的视图 => " + viewName);
            }
        }else{
            // 如果mav已经包含了视图对象了，就不需要查找了
            view = mav.getView();
            if (view == null) {
                throw new Exception("ModelAndView [" + mav + "] 即找不到视图模板，又缺少直接View对象，无法解析视图");
            }
        }
        view.render(mav, req, resp);
    }

    /**
     * 解析视图名称
     */
    private View resolveViewName(String viewName) throws Exception {
        View view = null;
        for (ViewResolver viewResolver : viewResolvers) {
            // 尝试解析这个view，可能返回null
            view = viewResolver.resolveViewName(viewName);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    /**
     * 获取handler对应的HandlerAdapter
     */
    private HandlerAdapter getHandlerAdapter(HandlerMapping handlerMapping){
        if (handlerAdapterMap.isEmpty()) return null;
        HandlerAdapter handlerAdapter = handlerAdapterMap.get(handlerMapping);
        if (handlerAdapter != null && handlerAdapter.supports(handlerMapping)){
            return handlerAdapter;
        }
        return null;
    }

    /**
     * 根据相应请求获取对应Handler
     */
    private HandlerMapping getHandlerMapping(HttpServletRequest req){
        if (handlerMappings.isEmpty()) return null;
        String url = req.getRequestURI();

        // contextPath是项目部署的url地址，需要替换为空字符
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        for (HandlerMapping handler : handlerMappings) {

            // 如果这个url被某个regex匹配到了，就返回这个对应的handler
            if (handler.getPattern().matcher(url).matches()){
                return handler;
            }
        }

        return null;
    }
}