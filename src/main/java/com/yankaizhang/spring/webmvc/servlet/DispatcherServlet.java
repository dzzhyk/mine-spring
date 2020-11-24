package com.yankaizhang.spring.webmvc.servlet;

import com.yankaizhang.spring.aop.support.AopUtils;
import com.yankaizhang.spring.beans.BeanWrapper;
import com.yankaizhang.spring.context.AnnotationConfigApplicationContext;
import com.yankaizhang.spring.context.annotation.Controller;
import com.yankaizhang.spring.webmvc.*;
import com.yankaizhang.spring.webmvc.annotation.RequestBody;
import com.yankaizhang.spring.webmvc.annotation.RequestMapping;
import com.yankaizhang.spring.webmvc.annotation.ResponseBody;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;
import com.yankaizhang.spring.webmvc.multipart.MultipartResolver;
import com.yankaizhang.spring.webmvc.multipart.commons.CommonsMultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * DispatcherServlet实现
 * @author dzzhyk
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
        log.debug("**********Dispatcher Servlet 初始化开始**********");
        initMultipartResolver(context);         // 多部分文件上传解析multipart

        initLocaleResolver(context);            // 本地化解析
        initThemeResolver(context);             // 主题解析

        initHandlerMappings(context);           // url映射到controller
        initHandlerAdapters(context);           // 多类型参数动态匹配，获得ModelAndView对象

        initHandlerExceptionResolvers(context);     // 运行异常处理
        initRequestToViewNameTranslator(context);   // 直接将请求解析到视图名
        initViewResolvers(context);             // 通过viewResolver将逻辑视图解析为具体视图实现
        initFlashMapManager(context);           // 初始化Flash映射管理器

        log.debug("singletonIoC容器实例个数 ===> " + String.valueOf(context.getSingletonIoc().size()) + " 个");
        log.debug("commonsIoC容器实例个数 ===> " + String.valueOf(context.getCommonIoc().size()) + " 个");
        log.debug("**********Dispatcher Servlet 初始化完成**********");
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
            e.printStackTrace();
        }
        if (multipartResolver != null){
            this.multipartResolver = multipartResolver;
            log.debug("获取了已配置MultipartResolver对象 => " + multipartResolver.getClass());
        }else{
            this.multipartResolver = new CommonsMultipartResolver();
            log.warn("未定义MultipartResolver对象，创建默认MultipartResolver => " + this.multipartResolver.getClass());
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
        Map<String, BeanWrapper> ioc = context.getCommonIoc();   // 获取已经存在的实例化好的对象
        try {
            for (BeanWrapper beanWrapper : ioc.values()) {
                Object beanInstance = beanWrapper.getWrappedInstance();
                if (beanInstance == null) continue;   // 排除可能有的bean没有在容器中

                Class<?> clazz = null;
                // 如果是Aop代理bean对象
                if (AopUtils.isAopProxy(beanInstance)){
                    clazz = AopUtils.getAopTarget(beanInstance);    // 如果是代理对象，需要获取到代理对象的最终目标类
                }else{
                    clazz = beanInstance.getClass();
                }

                if (clazz==null || !clazz.isAnnotationPresent(Controller.class)) continue;

                // 检查是否有ResponseBody注解，如果有说明是json形式数据返回
                if (clazz.isAnnotationPresent(ResponseBody.class)){
                    
                }


                String[] baseUrls = {};
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrls = requestMapping.value();   // 如果标注了@Controller注解的值
                }

                // 将controller标注@MyRequestMapping的方法加入handlerMapping
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
                                handlerMappings.add(new HandlerMapping(beanInstance, method, pattern));
                            }
                        }
                    }else{
                        for (String methodMapping : methodMappings) {
                            // 如果没有controller根路径，则空路径情况需要避免
                            if (!"".equals(methodMapping.trim())){
                                String url = ("/" + methodMapping).replaceAll("/+", "/");
                                Pattern pattern = Pattern.compile(url);
                                handlerMappings.add(new HandlerMapping(beanInstance, method, pattern));
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
     */
    private void initHandlerAdapters(AnnotationConfigApplicationContext context){
        for (HandlerMapping handlerMapping : handlerMappings) {
            handlerAdapterMap.put(handlerMapping, new HandlerAdapter());
            log.debug("Mapped: " + handlerMapping.getPattern() + " ===> " + handlerMapping.getMethod());
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
            log.debug("获取了已配置internalViewResolver对象 => " + internalViewResolver.toString());
        }else{
            ViewResolver resolver = new ViewResolver();
            log.warn("未配置viewResolver，将创建默认viewResolver => " + resolver.toString());
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
            resp.setContentType(View.DEFAULT_CONTENT_TYPE);
            StringBuffer buffer = new StringBuffer();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            buffer.append("<h1>500 Exception</h1>").append(now.format(dtf)).append("<br>mine-springframework 0.0.1-SNAPSHOT<br>");
            buffer.append("<h2>Error Message</h2>").append(e.getMessage()).append("<hr><h2>StackTrace</h2>");
            StackTraceElement[] stackTrace = e.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                buffer.append(stackTraceElement).append("\n");
            }
            resp.getWriter().write(buffer.toString());
            e.printStackTrace();
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String requestURI = req.getRequestURI();
        log.debug("收到请求 ===> " + requestURI);

        HttpServletRequest processedRequest = req;
        boolean multipartRequestParsed = false;
        processedRequest = checkMultipart(req);
        multipartRequestParsed = (processedRequest != req); // 如果两次解析出来的请求不是一个，说明是文件上传请求

        HandlerMapping handlerMapping = getHandlerMapping(processedRequest);
        if (null == handlerMapping){
            // 如果没有这个controller，返回404页面
            throw new Exception("没有该请求对应的 HandlerMapping 实现 => \"" + requestURI + "\"");
        }

        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        if (handlerAdapter == null){
            throw new Exception("没有该请求对应的 HandlerAdapter 实现 => \"" + requestURI + "\"");
        }
        ModelAndView model = handlerAdapter.handle(processedRequest, resp, handlerMapping);
        processDispatchResult(req, resp, model);

        // 清理上传产生的资源文件
        if (multipartRequestParsed) {
            if (this.multipartResolver != null){
                this.multipartResolver.cleanupMultipart((MultipartRequest) processedRequest);
            }
        }
    }

    /**
     * 检查请求是否为文件上传请求
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
    private void processDispatchResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView modelAndView)
            throws Exception {
        if (null == modelAndView) return;
        if (viewResolvers.isEmpty()) return;

        for (ViewResolver viewResolver : viewResolvers) {
            // 尝试解析这个view，可能返回null
            View view = viewResolver.resolveViewName(modelAndView.getViewName());
            if (view != null){
                view.render(modelAndView, viewResolver, req, resp);
                return;
            }
        }
    }

    /**
     * 获取handler对应的HandlerAdapter
     */
    private HandlerAdapter getHandlerAdapter(HandlerMapping handlerMapping){
        if (handlerAdapterMap.isEmpty()) return null;
        HandlerAdapter handlerAdapter = handlerAdapterMap.get(handlerMapping);
        if (handlerAdapter.supports(handlerMapping)){
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
        String contextPath = req.getContextPath();  // contextPath是项目部署的url地址，需要替换为空字符
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

