package com.yankaizhang.springframework.context;

/**
 * 这个类定义了注解配置注册的基本操作
 * 1. 扫描注解
 * 2. 注册
 */

public interface AnnotationConfigRegistry {

    /**
     * 扫描包路径
     */
    void scan(String... basePackages);

    /**
     * 注册配置类及其定义的Bean对象
     */
    void register(Class<?>... componentClasses);

}
