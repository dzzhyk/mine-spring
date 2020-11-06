package com.yankaizhang.springframework.webmvc.servlet;

import com.yankaizhang.springframework.aop.support.AopUtils;
import com.yankaizhang.springframework.beans.BeanWrapper;
import com.yankaizhang.springframework.context.AnnotationConfigApplicationContext;
import com.yankaizhang.springframework.webmvc.*;
import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.webmvc.annotation.RequestMapping;
import com.yankaizhang.springframework.webmvc.multipart.MultipartRequest;
import com.yankaizhang.springframework.webmvc.multipart.MultipartResolver;
import com.yankaizhang.springframework.webmvc.multipart.commons.CommonsMultipartResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 手写实现DispatcherServlet
 */
@SuppressWarnings("all")
@WebServlet(
        name = "dispatcherServlet",
        displayName = "com.yankaizhang.springframework",
        urlPatterns="/*",
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "contextConfigLocation", value = "classpath:application.properties")
        }
)
public class DispatcherServlet extends HttpServlet {

    public static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);
    private final String LOCATION = "contextConfigLocation";
    private MultipartResolver multipartResolver;    // 文件请求解析器
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private Map<HandlerMapping, HandlerAdapter> handlerAdapterMap = new HashMap<>();
    private List<ViewResolver> viewResolvers = new ArrayList<>();
    private AnnotationConfigApplicationContext context; // 内置容器

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 初始化IoC容器
        context = new AnnotationConfigApplicationContext(config.getInitParameter(LOCATION));
        initStrategies(context);
    }

    private void initStrategies(AnnotationConfigApplicationContext context){
        logger.debug("**********Dispatcher Servlet 初始化开始**********");

        initMultipartResolver(context); // 多部分文件上传解析multipart

        initLocaleResolver(context);    // 本地化解析
        initThemeResolver(context);     // 主题解析

        initHandlerMappings(context);       // url映射到controller
        initHandlerAdapters(context);       // 多类型参数动态匹配，获得ModelAndView对象

        initHandlerExceptionResolvers(context);     // 运行异常处理
        initRequestToViewNameTranslator(context);   // 直接将请求解析到视图名
        initViewResolvers(context);     // 通过viewResolver将逻辑视图解析为具体视图实现
        initFlashMapManager(context);   // 初始化Flash映射管理器

        logger.debug("singletonIoC容器实例个数 ===> " + String.valueOf(context.getSingletonIoc().size()) + " 个");
        logger.debug("commonsIoC容器实例个数 ===> " + String.valueOf(context.getCommonIoc().size()) + " 个");
        logger.debug("**********Dispatcher Servlet 初始化完成**********");
    }


    /**
     * 初始化文件解析器
     */
    private void initMultipartResolver(AnnotationConfigApplicationContext context){
        MultipartResolver multipartResolver = null;
        try {
            // 从容器中获取
            multipartResolver = (MultipartResolver) context.getBean("multipartResolver");

        }catch (Exception e){
            e.printStackTrace();
        }
        if (multipartResolver != null){
            this.multipartResolver = multipartResolver;
            logger.debug("获取了已配置MultipartResolver对象 => " + multipartResolver.getClass());
        }else{
            this.multipartResolver = new CommonsMultipartResolver();
            logger.debug("未定义MultipartResolver对象，创建默认MultipartResolver => " + this.multipartResolver.getClass());
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

                String baseUrl = "";
                if (clazz.isAnnotationPresent(RequestMapping.class)){
                    RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                    baseUrl = requestMapping.value();   // 如果标注了@MyController注解的值
                }

                // 将controller标注@MyRequestMapping的方法加入handlerMapping
                for (Method method : clazz.getDeclaredMethods()) {
                    if (!method.isAnnotationPresent(RequestMapping.class)) continue;

                    RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

                    // 这里生成的最终url应该是正则表达式形式
                    String url = ("/" + baseUrl + "/" + requestMapping.value()).replaceAll("/+", "/");
                    Pattern pattern = Pattern.compile(url);
                    handlerMappings.add(new HandlerMapping(beanInstance, method, pattern)); // 最后加入的都应该是代理对象
                    logger.debug("Mapped: " + url + " ===> " + method);
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
        }
    }


    /**
     * 注册模板解析器
     */
    private void initViewResolvers(AnnotationConfigApplicationContext context){
        String templateRoot = context.getConfig().getProperty("templateRoot");
        String templateRootPath = this.getClass().getClassLoader().getResource(templateRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        for (File template : templateRootDir.listFiles()) {
            viewResolvers.add(new ViewResolver(templateRoot, template.getName()));
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
            e.printStackTrace();
            try {
                ModelAndView modelAndView = new ModelAndView("500");
                HashMap<String, Object> map = new HashMap<>();
                map.put("stackTrace", Arrays.toString(e.getStackTrace()));
                modelAndView.setModel(map);
                processDispatchResult(req, resp, modelAndView); // 返回500渲染页面
            }catch (Exception e1){
                e1.printStackTrace();
                resp.getWriter().write("<h1>500 Exception</h1>\n Message：\n" +
                        e.getMessage() + "\nStackTrace：\n" + Arrays.toString(e.getStackTrace()));
            }
        }
    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        logger.debug("收到请求 ===> " + req.getRequestURI());

        HttpServletRequest processedRequest = req;
        boolean multipartRequestParsed = false;
        processedRequest = checkMultipart(req);
        multipartRequestParsed = (processedRequest != req); // 如果两次解析出来的请求不是一个，说明是文件上传请求

        HandlerMapping handlerMapping = getHandlerMapping(processedRequest);
        if (null == handlerMapping){
            // 如果没有这个controller，返回404页面
            processDispatchResult(req, resp, new ModelAndView("404"));
            return;
        }

        HandlerAdapter handlerAdapter = getHandlerAdapter(handlerMapping);
        if (handlerAdapter == null){
            throw new Exception("没有该请求对应的 HandlerAdapter 实现 => \"" + req.getRequestURI() + "\"");
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
                    logger.debug("上传文件解析失败", ex);
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
            if (!viewResolver.getViewName().equals(modelAndView.getViewName().trim()+ViewResolver.DEFAULT_TEMPLATE_SUFFIX)) continue;
            View view = viewResolver.resolveViewName(modelAndView.getViewName(), null);
            if (view != null){
                view.render(modelAndView.getModel(), req, resp);
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

