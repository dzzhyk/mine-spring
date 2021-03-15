package com.yankaizhang.spring.beans.factory.annotation;


import java.lang.annotation.*;

/**
 * 自动装配注解<br/>
 * 默认按照类型注入<br/>
 * Tips: Autowired注解仅支持注入属性，还不支持其他字段类型注入
 * @author dzzhyk
 * @since 2020-11-28 13:51:56
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
