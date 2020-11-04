package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类中定义Bean对象
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {

    /**
     * beanName
     */
    String value() default "";

}
