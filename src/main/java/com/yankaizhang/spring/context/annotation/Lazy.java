package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 用于标注Bean是否为懒加载
 * @author dzzhyk
 * @since 2020-11-28 13:49:03
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lazy {
}
