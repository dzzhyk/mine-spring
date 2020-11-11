package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * Service表示了服务层对象
 * 其本身也是组件{@link com.yankaizhang.springframework.context.annotation.Component}对象
 * @author dzzhyk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    String value() default "";
}
