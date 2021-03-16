package com.yankaizhang.spring.webmvc;

import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.method.support.ArgumentResolverComposite;
import com.yankaizhang.spring.web.method.support.InvocableHandlerMethod;
import com.yankaizhang.spring.web.method.support.ReturnValueResolverComposite;
import com.yankaizhang.spring.web.model.ModelAndView;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.resolver.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 处理请求传递的参数解析到controller中的方法
 * @author dzzhyk
 * @since 2020-11-28 13:39:53
 */
public class HandlerAdapter {

    /** 自定义的传入参数处理器 */
    private List<ArgumentResolver> customArgumentResolvers;

    /** 传入参数处理器对象集合类 */
    private final ArgumentResolverComposite argumentResolvers;

    /** 自定义的返回值处理器 */
    private List<ReturnValueResolver> customReturnValueResolvers;

    /** 返回值处理器对象集合类 */
    private final ReturnValueResolverComposite returnValueResolvers;

    public HandlerAdapter() {
        List<ArgumentResolver> defaultArgumentResolvers = getDefaultArgumentResolvers();
        List<ReturnValueResolver> defaultReturnValueResolvers = getDefaultReturnValueResolvers();

        this.argumentResolvers = new ArgumentResolverComposite().addResolver(defaultArgumentResolvers);
        this.returnValueResolvers = new ReturnValueResolverComposite().addResolver(defaultReturnValueResolvers);
    }

    /**
     * 检测是否支持
     */
    public boolean supports(Object handler){
        return (handler instanceof HandlerMapping);
    }

    public ModelAndView handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        // handlerMapping是经过封装的handler对象
        HandlerMapping handlerMapping = (HandlerMapping) handler;

        // 创建一个请求包装类WebRequest
        WebRequest webRequest = new WebRequest(req, resp);

        // 拿到需要处理的方法
        InvocableHandlerMethod invocableMethod = new InvocableHandlerMethod(handlerMapping.getMethod());

        // 给这个方法设置当前已知的输入输出解析器
        if (this.argumentResolvers != null) {
            invocableMethod.setArgumentResolvers(this.argumentResolvers);
        }
        if (this.returnValueResolvers != null) {
            invocableMethod.setReturnValueResolvers(this.returnValueResolvers);
        }

        // 返回的mav内容
        ModelAndViewBuilder mavBuilder = new ModelAndViewBuilder();

        // 真正执行方法
        invocableMethod.invokeAndHandle(webRequest, mavBuilder);

        return mavBuilder.build();
    }

    /**
     * 获取默认的ArgumentResolver列表
     */
    private List<ArgumentResolver> getDefaultArgumentResolvers() {
        List<ArgumentResolver> resolvers = new ArrayList<>();

        // TODO: 在这里继续添加内置的ArgumentResolver参数处理器
        resolvers.add(new ModelAndViewMethodResolver());
        resolvers.add(new RequestParamMethodArgumentResolver());
        resolvers.add(new RequestResponseBodyMethodResolver());
        resolvers.add(new SimpleClassMethodResolver());
        resolvers.add(new ServletRequestMethodArgumentResolver());
        resolvers.add(new ServletResponseMethodArgumentResolver());

        return resolvers;
    }

    /**
     * 获取默认的ArgumentResolver列表
     */
    private List<ReturnValueResolver> getDefaultReturnValueResolvers() {
        List<ReturnValueResolver> resolvers = new ArrayList<>();

        // TODO: 在这里继续添加内置的ReturnValueResolver返回值处理器
        resolvers.add(new ViewNameMethodReturnValueResolver());
        resolvers.add(new ModelAndViewMethodResolver());
        resolvers.add(new RequestResponseBodyMethodResolver());

        return resolvers;
    }
}
