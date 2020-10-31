package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";
}
