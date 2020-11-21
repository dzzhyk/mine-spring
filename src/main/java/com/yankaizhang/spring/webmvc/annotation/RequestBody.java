package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 用来标注json格式请求
 * @author dzzhyk
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {

    boolean required() default true;

}