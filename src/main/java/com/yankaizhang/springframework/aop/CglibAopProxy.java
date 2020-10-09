package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.intercept.MethodInvocation;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * CGLib动态代理实现
 */
@Slf4j
public class CglibAopProxy implements AopProxy, MethodInterceptor {

    private AdvisedSupport config;  // 获取代理配置文件封装

    public CglibAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        log.debug("创建CGLib动态代理目标 ===> " + this.config.getTargetClass().toString());
        return getProxy(this.config.getTargetClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        // cglib的工具类，用来创建子类（代理对象）
        Enhancer en = new Enhancer();
        // 设置代理对象父类
        en.setSuperclass(config.getTargetClass());
        en.setCallback(this);
        return en.create();
    }

    /**
     * intercept是执行代理方法的入口
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

        // 获取这个方法的拦截器链
        List<Object> interceptorsAdvices = config.getInterceptorsAdvice(method, this.config.getTargetClass());

        // 创建拦截器链的执行对象 MethodInvocation
        MethodInvocation invocation =
                new MethodInvocation(obj, method, this.config.getTarget(), this.config.getTargetClass(), args, interceptorsAdvices);
        
        // 执行这个拦截器链
        return invocation.proceed();
    }
}
