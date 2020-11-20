package com.yankaizhang.spring.aop.support;

import com.yankaizhang.spring.aop.AopProxy;
import com.yankaizhang.spring.aop.SpringProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

/**
 * Aop工具类
 * @author dzzhyk
 */
@SuppressWarnings("unused")
public class AopUtils {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    /**
     * 一些不需要代理的方法名
     */
    private static final String HASHCODE = "hashCode";
    private static final String EQUALS = "equals";
    private static final String TO_STRING = "toString";

    public static boolean isFinal(Method method){
        return Modifier.isFinal(method.getModifiers());
    }

    public static boolean isHashCode(Method method){
        return HASHCODE.equals(method.getName());
    }

    public static boolean isEquals(Method method){
        return EQUALS.equals(method.getName());
    }

    public static boolean isToString(Method method){
        return TO_STRING.equals(method.getName());
    }

    /**
     * 检查是否可以代理方法
     */
    public static boolean isInvokeAble(Method method){
        return !(isFinal(method) || isEquals(method) || isHashCode(method) || isToString(method));
    }

    /**
     * 判断是否为Aop代理对象
     */
    public static boolean isAopProxy(Object object) {
        return (object instanceof SpringProxy && (Proxy.isProxyClass(object.getClass()) ||
                object.getClass().getName().contains(CGLIB_CLASS_SEPARATOR)));
    }

    /**
     * 判断代理类型是否为Jdk动态代理
     */
    public static boolean isJdkDynamicProxy(Object object) {
        return (object instanceof SpringProxy && Proxy.isProxyClass(object.getClass()));
    }

    /**
     * 判断代理类型是否为CGLib动态代理
     */
    public static boolean isCglibProxy(Object object) {
        return (object instanceof SpringProxy &&
                object.getClass().getName().contains(CGLIB_CLASS_SEPARATOR));
    }

    /**
     * 获取代理对象的目标类对象
     */
    public static Class<?> getAopTarget(Object object){
        Class<?> result = null;
        // TODO：存在代理嵌套的情况，所以需要加循环
        if (isJdkDynamicProxy(object)){
            result = getSpringJdkProxyTarget(object);
            if (isAopProxy(result)){
                return getAopTarget(result);
            }
            return result;
        }else if (isCglibProxy(object)){
            result = getSpringCglibProxyTarget(object);
            if (isAopProxy(result)){
                return getAopTarget(result);
            }
            return result;
        }
        return result;
    }

    /**
     * 获取Spring自己产生的原生Jdk反射对象的源对象
     */
    public static Class<?> getSpringJdkProxyTarget(Object object){
        // Jdk
        Class<?> result = null;
        try {
            Field h = object.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            AopProxy jdkDynamicAopProxy = (AopProxy) h.get(object);

            Field config = jdkDynamicAopProxy.getClass().getDeclaredField("config");
            config.setAccessible(true);
            AdvisedSupport advisedSupport = (AdvisedSupport) config.get(jdkDynamicAopProxy);

            Field targetClass = advisedSupport.getClass().getDeclaredField("targetClass");
            targetClass.setAccessible(true);
            result = (Class<?>) targetClass.get(advisedSupport);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取Spring自己产生的CGLib代理对象源对象
     */
    public static Class<?> getSpringCglibProxyTarget(Object object){
        Class<?> result = null;
        try {
            // CGLib
            // 这里其实就是在一层层的深入获取属性
            Field h = object.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);

            Object obj = h.get(object);

            Field advisedSupport = obj.getClass().getDeclaredField("config");
            advisedSupport.setAccessible(true);

            // 获取真正的advisedSupport对象
            Object temp = advisedSupport.get(obj);

            // 获取目标类的Class
            Field targetClass = temp.getClass().getDeclaredField("targetClass");
            targetClass.setAccessible(true);
            result = (Class<?>) targetClass.get(temp);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
