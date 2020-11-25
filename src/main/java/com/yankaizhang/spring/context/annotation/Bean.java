package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类中定义Bean对象
 * @author dzzhyk
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * beanName
     */
    String value() default "";

    /**
     * bean初始化方法
     */
    String initMethod() default "";

    /**
     * bean销毁方法
     */
    String destroyMethod() default "";
}
