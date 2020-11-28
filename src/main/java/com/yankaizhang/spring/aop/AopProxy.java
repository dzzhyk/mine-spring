package com.yankaizhang.spring.aop;

/**
 * 代理工厂的顶层接口，提供获取代理对象的顶层入口
 * 实现子类：
 * <li>{@link CglibAopProxy}</li>
 * <li>{@link JdkDynamicAopProxy}</li>
 * @author dzzhyk
 * @since 2020-11-28 13:55:30
 */
public interface AopProxy {

    /**
     * 获取代理对象
     * @return 代理对象
     */
    Object getProxy();


    /**
     * 通过自定义类加载器获取代理对象
     * @param classLoader 自定义类加载器
     * @return 代理对象
     */
    Object getProxy(ClassLoader classLoader);
}
