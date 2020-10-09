package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.intercept.MethodInvocation;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

/**
 * Jdk动态代理实现
 */
@Slf4j
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private AdvisedSupport config;  // 获取代理配置文件封装

    public JdkDynamicAopProxy(AdvisedSupport config) {
        this.config = config;
    }

    /**
     * 调用目标类的类加载器获取原生类
     */
    @Override
    public Object getProxy() {
        log.debug("创建JDK动态代理目标 ===> " + this.config.getTargetClass().toString());
        return getProxy(this.config.getTargetClass().getClassLoader());
    }


    /**
     * 使用Jdk动态代理创建原生类的代理对象
     */
    @Override
    public Object getProxy(ClassLoader classLoader) {
        return Proxy.newProxyInstance(classLoader, this.config.getTargetClass().getInterfaces(), this);
    }

    /**
     * invoke方法是执行代理的入口
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取这个方法的拦截器链
        List<Object> interceptorsAdvices = config.getInterceptorsAdvice(method, this.config.getTargetClass());

        // 创建拦截器链的执行对象 MethodInvocation
        MethodInvocation invocation =
                new MethodInvocation(proxy, method, this.config.getTarget(), this.config.getTargetClass(), args, interceptorsAdvices);

        // 执行这个拦截器链
        return invocation.proceed();
    }
}
