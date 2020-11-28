package com.yankaizhang.spring.aop.aopanno;

import java.lang.annotation.*;

/**
 * 切点表达式
 * @author dzzhyk
 * @since 2020-11-28 13:53:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointCut {
    String value() default "";
}
