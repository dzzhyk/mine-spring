package com.yankaizhang.springframework.context.annotation;

import java.lang.annotation.*;

/**
 * 配置类注解
 * 其本身也是组件{@link com.yankaizhang.springframework.context.annotation.Component}对象
 * @author dzzhyk
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    String value() default "";

}
