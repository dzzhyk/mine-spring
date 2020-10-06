package com.yankaizhang.springframework.annotation;


import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FrameworkAnnotation
public @interface Autowired {
    String value() default "";
}
