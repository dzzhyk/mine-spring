package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 请求体，标注了此注解的内容会被解析到controller参数中
 * @author dzzhyk
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestBody {

    /**
     * 标识当前该参数是否是必须的
     */
    boolean required() default true;

}