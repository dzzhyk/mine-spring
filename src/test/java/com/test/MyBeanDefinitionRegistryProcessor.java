package com.test;

import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;


@Component
public class MyBeanDefinitionRegistryProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws Exception {
        System.out.println("BeanDefinitionRegistryProcessor -> 额外注册bean定义");
    }

    @Override
    public void postProcessBeanFactory(BeanFactory beanFactory) throws RuntimeException {

    }
}
