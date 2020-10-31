package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";

}
