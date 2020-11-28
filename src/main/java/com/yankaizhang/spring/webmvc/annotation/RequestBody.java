package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 请求体
 * 标注了此注解的参数，请求传送过来的参数会尽可能的被解析到目标对象中
 * @author dzzhyk
 * @since 2020-11-28 13:42:47
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