package com.yankaizhang.spring.web.method;


import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 视图控制器controller方法的包装类
 * @author dzzhyk
 * @since 2020-11-25 14:55:33
 */
public class HandlerMethod {

    public static final Logger log = LoggerFactory.getLogger(HandlerMethod.class);

    /** 真正的方法对象 */
    private final Method method;

    /** 方法参数列表 */
    private final MethodParameter[] methodParameters;

    /** 方法的注解列表 */
    private final Annotation[] methodAnnotations;

    private final Object bean;

    private final BeanFactory beanFactory;

    private final Class<?> beanType;

    public HandlerMethod(Object bean, Method method) {
        this.method = method;
        this.bean = bean;
        this.methodParameters = initMethodParameters();
        this.methodAnnotations = method.getAnnotations();
        this.beanFactory = null;
        this.beanType = ClassUtils.getUserClass(bean);
    }

    public HandlerMethod(Object bean, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException {
        this.method = bean.getClass().getMethod(methodName, parameterTypes);
        this.methodParameters = initMethodParameters();
        this.methodAnnotations = method.getAnnotations();
        this.bean = bean;
        this.beanFactory = null;
        this.beanType = ClassUtils.getUserClass(bean);
    }

    /**
     * 从另一个HandlerMethod对象创建
     * @param method HandlerMethod对象
     */
    public HandlerMethod(HandlerMethod method){
        this.method = method.method;
        this.methodParameters = method.methodParameters;
        this.methodAnnotations = method.methodAnnotations;
        this.bean = method.bean;
        this.beanType = method.beanType;
        this.beanFactory = method.beanFactory;
    }

    /**
     * 初始化该HandlerMethod对象获得方法参数
     */
    private MethodParameter[] initMethodParameters(){
        int count = this.method.getParameterCount();
        MethodParameter[] temp = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            temp[i] = new MethodParameter(this.method, i);
        }
        return temp;
    }

    public Method getMethod() {
        return this.method;
    }

    public MethodParameter[] getMethodParameters() {
        return this.methodParameters;
    }

    public Annotation[] getMethodAnnotations() {
        return this.methodAnnotations;
    }

    public Object getBean() {
        return bean;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    public Class<?> getBeanType() {
        return beanType;
    }
}
