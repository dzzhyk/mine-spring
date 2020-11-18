package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * @author dzzhyk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
