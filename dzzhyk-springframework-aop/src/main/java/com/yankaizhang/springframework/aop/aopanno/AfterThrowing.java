package com.yankaizhang.springframework.aop.aopanno;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterThrowing {
    String value() default "";
    String exception() default "java.lang.Exception";
}