package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类注解
 * 其本身也是组件{@link Component}对象
 * @author dzzhyk
 * @since 2020-11-28 13:48:11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";

}
