package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * CGLib动态代理实现
 */
@Slf4j
public class CglibAopProxy implements AopProxy, InvocationHandler, MethodInterceptor {

    private AdvisedSupport config;  // 获取代理配置文件封装

    public CglibAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        log.info("创建CGLib动态代理目标 ===> " + this.config.getTargetClass().toString());
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

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    /**
     * intercept是执行代理方法的入口
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        // 执行原对象的方法
        Object returnValue = methodProxy.invokeSuper(obj, args);
        // 代理执行内容
        log.info("执行CGLib代理对象代理方法 ===> " + method.getName());
        return returnValue;
    }
}
