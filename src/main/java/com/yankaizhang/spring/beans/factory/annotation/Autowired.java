package com.yankaizhang.spring.beans.factory.annotation;


import java.lang.annotation.*;

/**
 * 自动装配注解
 * @author dzzhyk
 * @since 2020-11-28 13:51:56
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
