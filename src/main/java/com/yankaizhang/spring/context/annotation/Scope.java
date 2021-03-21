package com.yankaizhang.spring.context.annotation;


import java.lang.annotation.*;

/**
 * 标识bean对象的作用域<br/>
 * 目前只支持单例、原型两种
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

    /**
     * 作用域类型名称<br/>
     * singleton、prototype
     */
    String value() default "";

}
