package com.yankaizhang.spring.web.method.support;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.ObjectUtils;
import com.yankaizhang.spring.util.ReflectionUtils;
import com.yankaizhang.spring.web.method.HandlerMethod;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;


/**
 * {@link HandlerMethod} 的一个扩展类
 * 这个类在invoke真正的方法之前，根据需要被处理的参数
 * 在已知的{@link ArgumentResolver}对象集合中寻找到一个可以处理的，然后处理该参数
 * 处理完成之后再invoke该方法
 * @author dzzhyk
 * @since 2020-11-28 13:44:43
 */
public class InvocableHandlerMethod extends HandlerMethod {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    /** 空参数常量 */
    private static final Object[] EMPTY_ARGS = new Object[0];

    /** 传入参数处理器对象集合类 */
    private ArgumentResolverComposite argumentResolvers;

    /** 返回值处理器对象集合类 */
    private ReturnValueResolverComposite returnValueResolvers;

    /** 该handlerMethod方法是否合法 */
    private boolean valid = true;

    public InvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public InvocableHandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        super(bean, methodName, parameterTypes);
    }

    /**
     * 从一个{@link HandlerMethod}对象创建该对象
     */
    public InvocableHandlerMethod(HandlerMethod method) {
        super(method);
        validate();
    }

    public ArgumentResolverComposite getArgumentResolvers() {
        return argumentResolvers;
    }

    public void setArgumentResolvers(ArgumentResolverComposite argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    public ReturnValueResolverComposite getReturnValueResolvers() {
        return returnValueResolvers;
    }

    public void setReturnValueResolvers(ReturnValueResolverComposite returnValueResolvers) {
        this.returnValueResolvers = returnValueResolvers;
    }

    /**
     * 执行方法
     */
    public void invokeAndHandle(WebRequest webRequest, ModelAndViewBuilder mav) throws Exception {

        // 调用传参解析器解析传入参数，然后执行这个方法，拿到原始返回值
        Object returnValue = invokeForRequest(webRequest);

        // 如果没有返回值
        if (null == returnValue){
            return;
        }

        // 调用返回值处理器来包装处理得到最终的返回值对象到一个ModelAndView
        this.returnValueResolvers.resolveReturnValue(returnValue,
                new MethodParameter.ReturnValueMethodParameter(this, returnValue), mav, webRequest);
    }

    /**
     * 解析请求参数，并且使用这些参数执行目标方法
     */
    private Object invokeForRequest(WebRequest request) throws Exception {
        Object[] args = getMethodArgumentValues(request);
        log.debug("参数 : " + Arrays.toString(args));
        return doInvoke(args);
    }


    /**
     * 获取方法参数值，并且使用可能的{@link ArgumentResolver}实现类来处理参数
     */
    private Object[] getMethodArgumentValues(WebRequest webRequest) throws Exception {
        MethodParameter[] methodParameters = getMethodParameters();
        if (ObjectUtils.isEmpty(methodParameters)){
            return EMPTY_ARGS;
        }

        // 如果方法参数列表不为空，就开始处理参数了！
        Object[] args = new Object[methodParameters.length];

        // 这里不要第一个返回值参数
        for (int i = 0; i < methodParameters.length; i++) {
            MethodParameter parameter = methodParameters[i];

            // 检查当前resolver集合是否有解析器内容
            if (!this.argumentResolvers.supportsParameter(parameter)) {
                throw new Exception("当前resolvers对于参数 => " + parameter.getParameterName() + " 缺少解析器");
            }

            // 如果找到了合适的解析器，就使用该解析器解析得到该参数对象
            args[i] = this.argumentResolvers.resolveArgument(parameter, webRequest);
        }

        return args;
    }

    /**
     * 使用处理过的参数，真正调用该方法
     * @param args 处理过的参数
     * @return 调用方法产生的原始结果
     */
    private Object doInvoke(Object[] args) throws Exception {
        Method method = getMethod();
        ReflectionUtils.makeAccessible(method);
        Object result;
        try {
            result = method.invoke(getBean(), args);
        } catch (IllegalAccessException e) {

            log.error("invoke非法访问 => " + method.getName());
            throw new Exception("invoke方法出错，非法访问 => " + method.getName());

        } catch (InvocationTargetException e) {

            log.error("invoke方法出错 => " + method.getName());
            throw new Exception("invoke方法出错 => " + method.getName());

        }
        return result;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public void validate() {
        try {
            // 检查对象
            int cnt = 0;
            MethodParameter[] parameters = getMethodParameters();
            for (MethodParameter parameter : parameters) {
                if (parameter.hasParameterAnnotation(RequestBody.class)){
                    ++cnt;
                }
                if (cnt > 1){
                    throw new Exception("同一方法中 @RequestBody请求体对象只能出现1次 => "
                            + getMethod().getDeclaringClass() + "." + getMethod().getName());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
