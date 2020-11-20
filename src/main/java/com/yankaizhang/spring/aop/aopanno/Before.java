package com.yankaizhang.spring.aop.aopanno;


import java.lang.annotation.*;

/**
 * 前置通知
 * @author dzzhyk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
    String value() default "";
}
