package com.yankaizhang.springframework.annotation.aopanno;

import java.lang.annotation.*;

/**
 * 切面注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    String value() default "";
}
