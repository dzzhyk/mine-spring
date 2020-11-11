package com.yankaizhang.springframework.context.annotation;


import java.lang.annotation.*;

/**
 * 元注解
 * @author dzzhyk
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
