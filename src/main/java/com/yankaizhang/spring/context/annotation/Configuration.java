package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类注解
 * 其本身也是组件{@link Component}对象
 * @author dzzhyk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";

}
