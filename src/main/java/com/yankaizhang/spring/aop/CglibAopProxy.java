package com.yankaizhang.spring.aop;

import com.yankaizhang.spring.aop.intercept.MethodInvocation;
import com.yankaizhang.spring.aop.support.AdvisedSupport;
import com.yankaizhang.spring.aop.support.AopUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;

/**
 * CGLib动态代理实现
 * @author dzzhyk
 */
public class CglibAopProxy implements AopProxy, MethodInterceptor {
    public static Logger log = LoggerFactory.getLogger(CglibAopProxy.class);
    /**
     * 获取代理配置文件封装
     */
    private AdvisedSupport config;

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
        // 给代理类实现SpringProxy接口
        en.setInterfaces(new Class[]{SpringProxy.class});
        en.setCallback(this);
        return en.create();
    }

    /**
     * intercept是执行代理方法的入口
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {

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
