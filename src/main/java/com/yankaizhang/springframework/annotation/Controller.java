package com.yankaizhang.springframework.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FrameworkAnnotation
public @interface Controller {
    String value() default "";
}
