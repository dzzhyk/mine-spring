package com.yankaizhang.spring.webmvc.annotation;

import java.lang.annotation.*;

/**
 * 响应体<br/>
 * 带有该注解返回的内容解析为json数据
 * @author dzzhyk
 * @since 2020-11-28 13:43:02
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
    
}
