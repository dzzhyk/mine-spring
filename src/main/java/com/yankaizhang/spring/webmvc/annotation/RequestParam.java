package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 指明Controller方法的参数
 * 在进行Controller解析的时候只解析标注了该注解的参数
 * @author dzzhyk
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    /**
     * 参数的名称name
     */
    String value() default "";
}
