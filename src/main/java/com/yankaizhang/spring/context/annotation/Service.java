package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * Service表示了服务层对象
 * 其本身也是组件{@link Component}对象
 * @author dzzhyk
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    String value() default "";
}
