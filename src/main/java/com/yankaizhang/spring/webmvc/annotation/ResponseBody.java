package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 响应体ResponseBody
 * 带有该注解返回的内容解析为json数据
 * @author dzzhyk
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
    
}
