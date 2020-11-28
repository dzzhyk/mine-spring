package com.yankaizhang.spring.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 反射相关操作工具类
 * @author dzzhyk
 * @since 2020-11-28 13:46:27
 */
@SuppressWarnings("all")
public class ReflectionUtils {

    /**
     * 尝试允许某个方法对象访问
     */
    public static void makeAccessible(Method method){
        int methodModifier = method.getModifiers();
        int methodClassModifier = method.getDeclaringClass().getModifiers();
        if ((!Modifier.isPublic(methodModifier) ||
                !Modifier.isPublic(methodClassModifier)) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    public static void makeAccessible(Constructor constructor){
        int methodModifier = constructor.getModifiers();
        int methodClassModifier = constructor.getDeclaringClass().getModifiers();
        if ((!Modifier.isPublic(methodModifier) ||
                !Modifier.isPublic(methodClassModifier)) && !constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
    }

}
