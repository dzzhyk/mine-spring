package com.yankaizhang.spring.beans.factory.annotation;


import java.lang.annotation.*;

/**
 * 自动装配注解
 * @author dzzhyk
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";
}
