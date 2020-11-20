package com.yankaizhang.spring.aop.intercept;

/**
 * 方法拦截器顶层接口
 * @author dzzhyk
 */
public interface MethodInterceptor {

    /**
     * 执行代理
     * @param methodInvocation 拦截器链
     * @return  代理方法执行结果
     * @throws Throwable 代理方法中的执行异常
     */
    Object invoke(MethodInvocation methodInvocation) throws Throwable;

}
