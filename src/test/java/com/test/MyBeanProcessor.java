package com.test;

import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyBeanProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws RuntimeException {
        if (bean instanceof MyComponent) {
            System.out.println("BeanProcessor -> 初始化前处理bean");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws RuntimeException {
        if (bean instanceof MyComponent) {
            System.out.println("BeanProcessor -> 初始化后处理bean");
        }
        return bean;
    }
}
