package com.yankaizhang.springframework.annotation;


import java.lang.annotation.*;

/**
 * 元注解
 */
@Target(ElementType.ANNOTATION_TYPE)
@Documented
@Inherited
public @interface FrameworkAnnotation {
    String value() default "";
}
