package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 组件扫描注解
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    String[] value() default "";

}
