package com.yankaizhang.spring.aop.aopanno;

import java.lang.annotation.*;

/**
 * 返回通知
 * @author dzzhyk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterReturning {
    String value() default "";
}
