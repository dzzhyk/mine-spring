package com.yankaizhang.spring.beans.factory.annotation;


import com.yankaizhang.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;

import java.lang.annotation.*;

/**
 * 自动装配注解<br/>
 * 默认按照类型注入<br/>
 * Tips: Autowired注解仅支持属性注入，还不支持构造器、setter注入<br/>
 * 注意：不允许对{@link BeanFactoryPostProcessor} 或 {@link BeanPostProcessor}中的内容进行注入
 * @author dzzhyk
 * @since 2020-11-28 13:51:56
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
}
