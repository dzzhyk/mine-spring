package com.yankaizhang.springframework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * Controller路径注解
 * @author dzzhyk
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * Controller路径
     */
    String[] value() default "";

}
