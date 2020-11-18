package com.yankaizhang.springframework.aop.aopanno;

import java.lang.annotation.*;

/**
 * 切点表达式
 * @author dzzhyk
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointCut {
    String value() default "";
}
