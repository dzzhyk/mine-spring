package com.yankaizhang.springframework.aop;

import com.yankaizhang.springframework.aop.intercept.MethodInvocation;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import com.yankaizhang.springframework.aop.support.AopUtils;
import com.yankaizhang.springframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;

/**
 * Jdk动态代理实现
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    public static final Logger log = LoggerFactory.getLogger(JdkDynamicAopProxy.class);
    /**
     * 获取代理配置文件封装
     */
    private AdvisedSupport config;

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
        Class<?>[] classes = new Class<?>[length+1];
        // 为代理对象添加SpringProxy接口，以此识别Spring代理对象
        ObjectUtils.addObjectToArray(classes, SpringProxy.class);
        return Proxy.newProxyInstance(classLoader, classes, this);
    }

    /**
     * invoke方法是执行代理的入口
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (!AopUtils.isInvokeAble(method)){
            return method.invoke(proxy, args);
        }

        // 获取这个方法的拦截器链
        List<Object> interceptorsAdvices = config.getInterceptorsAdvice(method, this.config.getTargetClass());

        // 创建拦截器链的执行对象 MethodInvocation
        MethodInvocation invocation =
                new MethodInvocation(proxy, method, this.config.getTarget(), this.config.getTargetClass(), args, interceptorsAdvices);

        log.debug(method.getName() + " 方法 获取拦截器链 ===>");
        for (int i = 0; i < interceptorsAdvices.size(); i++) {
            log.debug(i + " ==> " + interceptorsAdvices.get(i).getClass());
        }
        log.debug("==> 执行以上拦截器链...");

        // 执行这个拦截器链
        return invocation.proceed();
    }
}
