package com.yankaizhang.springframework.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

/**
 * 注解相关的工具类
 * @author dzzhyk
 */

public class AnnotationUtils {

    /**
     * 获取注解反射类的源对象
     */
    public static Class<?> getAnnotationJdkProxyTarget(Object object){
        Class<?> result = null;
        try {
            Field h = object.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            InvocationHandler handler = (InvocationHandler) h.get(object);

            Field config = handler.getClass().getDeclaredField("type");
            config.setAccessible(true);
            result = (Class<?>) config.get(handler);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

}
