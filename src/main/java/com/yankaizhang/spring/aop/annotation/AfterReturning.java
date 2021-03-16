package com.yankaizhang.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * 返回通知
 * @author dzzhyk
 * @since 2020-11-28 13:53:33
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AfterReturning {
}
