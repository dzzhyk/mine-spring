package com.test;

import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyBeanFactoryProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) throws RuntimeException {
        System.out.println("BeanFactoryProcessor -> 处理beanFactory");
    }
}
