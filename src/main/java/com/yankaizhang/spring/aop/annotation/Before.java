package com.yankaizhang.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * 前置通知
 * @author dzzhyk
 * @since 2020-11-28 13:53:46
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Before {
}