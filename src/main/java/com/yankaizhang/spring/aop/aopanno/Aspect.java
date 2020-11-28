package com.yankaizhang.spring.aop.aopanno;

import java.lang.annotation.*;

/**
 * 切面注解
 * @author dzzhyk
 * @since 2020-11-28 13:53:43
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    String value() default "";
}
