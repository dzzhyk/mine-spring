package com.yankaizhang.spring.context;

/**
 * 这个类定义了注解配置注册的基本操作
 * 1. 扫描注解
 * 2. 注册
 * @author dzzhyk
 * @since 2020-11-28 13:51:29
 */

public interface AnnotationConfigRegistry {

    /**
     * 扫描包路径
     * @param basePackages 包路径列表
     */
    void scan(String... basePackages);

    /**
     * 注册配置类及其定义的Bean对象
     * @param componentClasses 配置类列表
     */
    void register(Class<?>... componentClasses);

}
