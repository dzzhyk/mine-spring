package com.yankaizhang.spring.util;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * bean对象操作工具类
 * @author dzzhyk
 */
public class BeanUtils {

    /** 默认的基本类型对象 */
    private static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;

    static {
        Map<Class<?>, Object> values = new HashMap<>();
        values.put(boolean.class, false);
        values.put(byte.class, (byte) 0);
        values.put(short.class, (short) 0);
        values.put(int.class, 0);
        values.put(long.class, (long) 0);
        DEFAULT_TYPE_VALUES = Collections.unmodifiableMap(values);
    }

    /**
     * 实例化某个类对象
     */
    public static <T> T instantiateClass(Class<T> clazz) throws Exception {
        assert clazz != null;
        if (clazz.isInterface()) {
            throw new Exception("不能实例化接口类Class");
        }
        try {
            return instantiateClass(clazz.getDeclaredConstructor());
        } catch (Exception ex) {
            throw new Exception("未找到类对象默认构造器 => " + clazz);
        }
    }

    /**
     * 通过构造器和参数实例化某个类对象
     * @param ctor 类构造器
     * @param args 可能的构造器参数
     * @param <T>  类元
     * @return 创建好的类对象
     * @throws Exception 创建过程中抛出的异常
     */
    public static <T> T instantiateClass(Constructor<T> ctor, Object... args) throws Exception {
        assert ctor != null;
        try {
            ReflectionUtils.makeAccessible(ctor);
            Class<?>[] parameterTypes = ctor.getParameterTypes();

            assert args.length <= parameterTypes.length;

            Object[] argsWithDefaultValues = new Object[args.length];
            for (int i = 0 ; i < args.length; i++) {
                if (args[i] == null) {
                    Class<?> parameterType = parameterTypes[i];
                    argsWithDefaultValues[i] = (parameterType.isPrimitive() ? DEFAULT_TYPE_VALUES.get(parameterType) : null);
                }
                else {
                    argsWithDefaultValues[i] = args[i];
                }
            }
            return ctor.newInstance(argsWithDefaultValues);

        }
        catch (InstantiationException ex) {
            throw new Exception("使用构造器方法实例化Class对象错误");
        }
    }

    /**
     * 判断某个类是不是一些简易的类，比如{@link String}或者{@link List}，但是不是void类型
     */
    public static boolean isSimpleValueType(Class<?> type){
        if (type.isArray()){
            return isSimpleValueType(type.getComponentType());
        }
        return (Void.class != type && void.class != type &&
                (Enum.class.isAssignableFrom(type) ||
                        CharSequence.class.isAssignableFrom(type) ||
                        Number.class.isAssignableFrom(type) ||
                        Date.class.isAssignableFrom(type) ||
                        Temporal.class.isAssignableFrom(type) ||
                        URI.class == type ||
                        URL.class == type ||
                        Locale.class == type ||
                        Class.class == type));
    }
}
