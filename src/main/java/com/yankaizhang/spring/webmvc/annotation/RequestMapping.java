package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * Controller路径注解
 * @author dzzhyk
 * @since 2020-11-28 13:42:53
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * Controller路径
     * @return controller路径
     */
    String[] value() default "";

}
