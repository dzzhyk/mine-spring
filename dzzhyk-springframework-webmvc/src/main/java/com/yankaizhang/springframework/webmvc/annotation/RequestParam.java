package com.yankaizhang.springframework.webmvc.annotation;

import java.lang.annotation.*;

/**
 * @author dzzhyk
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
