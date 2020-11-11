package com.yankaizhang.springframework.aop.aspect;

import com.yankaizhang.springframework.aop.intercept.MethodInterceptor;
import com.yankaizhang.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 前置通知
 */
public class MethodBeforeAdvice extends AbstractAspectjAdvice implements Advice, MethodInterceptor {

    private JoinPoint joinPoint;

    public MethodBeforeAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    public void before(Method method, Object[] arguments, Object target) throws Throwable{
        invokeAdviceMethod(joinPoint, null, null);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        this.joinPoint = methodInvocation;
        this.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getTarget());
        return methodInvocation.proceed();
    }
}
