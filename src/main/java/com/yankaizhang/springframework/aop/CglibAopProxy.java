package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * CGLib动态代理实现
 */
public class CglibAopProxy implements AopProxy, InvocationHandler {

    private AdvisedSupport config;  // 获取代理配置文件封装

    public CglibAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }
}
