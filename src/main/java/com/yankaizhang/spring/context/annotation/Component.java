package com.yankaizhang.spring.context.annotation;


import java.lang.annotation.*;

/**
 * 元注解
 * @author dzzhyk
 * @since 2020-11-28 13:47:56
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
