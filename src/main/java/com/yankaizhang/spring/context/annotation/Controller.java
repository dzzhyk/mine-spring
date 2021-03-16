package com.yankaizhang.spring.context.annotation;

import java.lang.annotation.*;

/**
 * 控制器对象，本身也是{@link Component}对象<br/>
 * <li>{@link Service}</li>
 * @author dzzhyk
 * @since 2020-11-28 13:48:15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Controller {
    String value() default "";
}
