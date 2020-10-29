package com.yankaizhang.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 封装拦截器回调的通用逻辑
 */
public abstract class AbstractAspectJAdvice implements Advice {

    private Method aspectMethod;
    private Object aspectTarget;

    public AbstractAspectJAdvice(Method aspectMethod, Object aspectTarget) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
    }

    protected Object invokeAdviceMethod(JoinPoint joinPoint, Object returnValue, Throwable exception) throws Throwable{
        Class<?>[] parameterTypes = this.aspectMethod.getParameterTypes();
        if (parameterTypes.length == 0){
            // 如果切入没有参数
            return this.aspectMethod.invoke(aspectTarget);
        }else{
            // 如果切入有参数
            Object[] arguments = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i] == JoinPoint.class){
                    arguments[i] = joinPoint;
                }else if (parameterTypes[i] == Throwable.class){
                    arguments[i] = exception;
                }else if (parameterTypes[i] == Object.class){
                    arguments[i] = returnValue;
                }
            }
            return this.aspectMethod.invoke(aspectTarget, arguments);
        }
    }
}
