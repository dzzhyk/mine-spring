package com.yankaizhang.springframework.aop.aspect;

import com.yankaizhang.springframework.aop.intercept.MethodInterceptor;
import com.yankaizhang.springframework.aop.intercept.MethodInvocation;

import java.lang.reflect.Method;


/**
 * 异常通知
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
            invokeAdviceMethod(methodInvocation, null, ex);  // 在方法调用过程中拦截异常
            throw ex;
        }
    }

    public void afterThrowing(){

    }

    public void setThrowingName(String throwingName) {
        this.throwingName = throwingName;
    }
}
