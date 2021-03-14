package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;


/**
 * Import注解显示指定一个或者多个需要向容器中加载的类对象<br/>
 * 在这里，mine-spring目前仅支持传入class对象创建，不支持selector和registrar
 * @author dzzhyk
 * @since 2021-03-14 12:12:01
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

    /**
     * 一个或者多个类对象
     */
    Class<?>[] value();
}
