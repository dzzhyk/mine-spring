package com.yankaizhang.springframework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @author dzzhyk
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
