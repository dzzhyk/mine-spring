package com.yankaizhang.spring.aop.aopanno;

import java.lang.annotation.*;

/**
 * 切面注解
 * @author dzzhyk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aspect {
    String value() default "";
}
