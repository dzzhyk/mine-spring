package com.yankaizhang.springframework.aop.intercept;

/**
 * 方法拦截器顶层接口
 */
public interface MethodInterceptor {

    Object invoke(MethodInvocation methodInvocation) throws Throwable;

}
