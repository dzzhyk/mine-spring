package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 组件扫描注解
 * @author dzzhyk
 * @since 2020-11-28 13:48:00
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    String[] value() default "";

}
