package com.yankaizhang.springframework.annotation;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Autowired {
    String value() default "";
}
