package com.yankaizhang.spring.aop.aspect;

import com.yankaizhang.spring.aop.intercept.MethodInterceptor;
import com.yankaizhang.spring.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;


/**
 * 异常通知
 * @author dzzhyk
 * @since 2020-11-28 13:54:24
 */
public class AfterThrowingAdvice extends AbstractAspectjAdvice implements Advice, MethodInterceptor {

    private String throwingName;
    private JoinPoint joinPoint;

    public AfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            return methodInvocation.proceed();
        }catch (Throwable ex){
            // 在方法调用过程中拦截异常
            invokeAdviceMethod(methodInvocation, null, ex);
            throw ex;
        }
    }

    public void afterThrowing(){

    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }
}
