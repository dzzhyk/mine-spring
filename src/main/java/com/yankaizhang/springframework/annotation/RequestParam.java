package com.yankaizhang.springframework.annotation;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FrameworkAnnotation
public @interface RequestParam {
    String value() default "";
}
