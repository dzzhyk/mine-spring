package com.yankaizhang.springframework.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FrameworkAnnotation
public @interface RequestMapping {
    String value() default "";
}
