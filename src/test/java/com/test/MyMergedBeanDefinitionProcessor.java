package com.test;

import com.yankaizhang.spring.beans.factory.config.MergedBeanDefinitionPostProcessor;
import com.yankaizhang.spring.beans.factory.generic.GenericBeanDefinition;
import com.yankaizhang.spring.context.annotation.Component;

@Component
public class MyMergedBeanDefinitionProcessor implements MergedBeanDefinitionPostProcessor {
    @Override
    public void postProcessMergedBeanDefinition(GenericBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
        if (beanType.equals(MyComponent.class)){
            System.out.println("MergedBeanDefinitionProcessor -> 合并bean定义");
        }
    }
}
