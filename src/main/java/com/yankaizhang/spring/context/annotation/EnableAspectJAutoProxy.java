package com.yankaizhang.spring.context.annotation;

import com.yankaizhang.spring.aop.support.AutoProxyCreator;

import java.lang.annotation.*;


/**
 * AOP启动注解<br/>
 * 作用是将{@link com.yankaizhang.spring.aop.support.AutoProxyCreator}类对象加入
 * @author dzzhyk
 * @since 2021-03-16 10:01:33
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoProxyCreator.class)
public @interface EnableAspectJAutoProxy {
}
