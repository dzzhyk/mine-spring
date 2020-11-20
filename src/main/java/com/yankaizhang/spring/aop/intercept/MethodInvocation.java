package com.yankaizhang.spring.aop.intercept;

import com.yankaizhang.spring.aop.aspect.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;


/**
 * 执行整个拦截器链
 * @author dzzhyk
 */
public class MethodInvocation implements JoinPoint {

    public static final Logger log = LoggerFactory.getLogger(MethodInvocation.class);

    /**
     * 代理对象
     */
    private Object proxy;
    /**
     * 代理的目标方法
     */
    private Method method;
    /**
     * 代理的目标对象
     */
    private Object target;
    /**
     * 代理的目标类
     */
    private Class<?> targetClass;
    /**
     * 方法实参列表
     */
    private Object[] arguments;
    /**
     * 拦截方法列表
     */
    private List<Object> interceptors;

    /**
     * 当前执行拦截器的位置
     */
    private int currentInterceptorIndex = -1;

    public MethodInvocation(Object proxy, Method method, Object target, Class<?> targetClass, Object[] arguments, List<Object> interceptors) {
        this.proxy = proxy;
        this.method = method;
        this.target = target;
        this.targetClass = targetClass;
        this.arguments = arguments;
        this.interceptors = interceptors;
    }

    public Object proceed() throws Throwable {
        if (currentInterceptorIndex == this.interceptors.size() -1){
            // 如果没有拦截器或者已经执行完毕了
            // 就正常执行方法
            log.debug("执行方法 ==> " + method.getName());
            return this.method.invoke(this.target, this.arguments);
        }

        Object interceptor = this.interceptors.get(++currentInterceptorIndex);

        if (interceptor instanceof MethodInterceptor){
            // 如果是定义的拦截器
            MethodInterceptor methodInterceptor = (MethodInterceptor) interceptor;
            return methodInterceptor.invoke(this);
        }else{
            return proceed();
        }
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Object getTarget() {
        return this.target;
    }
}
