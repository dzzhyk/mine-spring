package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * Service表示了服务层对象
 * 其本身也是组件{@link Component}对象
 * <li>{@link Controller}</li>
 * @author dzzhyk
 * @since 2020-11-28 13:49:11
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {
    String value() default "";
}
