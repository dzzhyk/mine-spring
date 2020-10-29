package com.yankaizhang.springframework.aop.aopanno;

import java.lang.annotation.*;

/**
 * 切点表达式
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointCut {
    String value() default "";
}
