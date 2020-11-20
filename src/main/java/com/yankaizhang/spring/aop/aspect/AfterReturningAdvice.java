package com.yankaizhang.spring.aop.aspect;

import com.yankaizhang.spring.aop.intercept.MethodInterceptor;
import com.yankaizhang.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * 后置通知
 */
public class AfterReturningAdvice extends AbstractAspectjAdvice implements Advice, MethodInterceptor {

    private JoinPoint joinPoint;

    public AfterReturningAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }



    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Object returnValue = methodInvocation.proceed();
        this.joinPoint = methodInvocation;
        // 在proceed之后进行后置通知
        this.afterReturning(returnValue, methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getTarget());
        return returnValue;
    }

    /**
     * 处理
     */
    public void afterReturning(Object returnValue, Method method, Object[] arguments, Object target) throws Throwable{
        invokeAdviceMethod(joinPoint, returnValue, null);
    }
}
