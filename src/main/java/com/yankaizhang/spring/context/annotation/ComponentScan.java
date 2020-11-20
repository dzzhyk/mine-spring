package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 组件扫描注解
 * @author dzzhyk
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ComponentScan {

    String[] value() default "";

}
