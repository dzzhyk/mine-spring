package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.intercept.MethodInvocation;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

/**
 * Jdk动态代理实现
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JdkDynamicAopProxy.class);
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
        int length = this.config.getTargetClass().getInterfaces().length;
        Class<?>[] interfaces = this.config.getTargetClass().getInterfaces();
        Class<?>[] classes = new Class<?>[length+1];    // 中间数组
        System.arraycopy(interfaces, 0, classes, 0, length);
        classes[length] = SpringProxy.class;
        return Proxy.newProxyInstance(classLoader, classes, this);
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

        if (!method.getName().equals("toString")){
            log.debug(method.getName() + " 方法 获取拦截器链 ===>");
            for (int i = 0; i < interceptorsAdvices.size(); i++) {
                log.debug(i + " ==> " + interceptorsAdvices.get(i).getClass());
            }
            log.debug("==> 执行以上拦截器链...");
        }

        // 执行这个拦截器链
        return invocation.proceed();
    }
}
