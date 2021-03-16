package com.yankaizhang.spring.aop.annotation;

import java.lang.annotation.*;

/**
 * 切点表达式<br/>
 * 示例: {@code com.yankaizhang.springframework.test.service.impl.TestServiceImpl.*(*)}
 * @author dzzhyk
 * @since 2020-11-28 13:53:50
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PointCut {
    String value() default "";
}
