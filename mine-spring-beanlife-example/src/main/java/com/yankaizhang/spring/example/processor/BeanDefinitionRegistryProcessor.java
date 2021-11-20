package com.yankaizhang.spring.example.processor;

import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import com.yankaizhang.spring.context.annotation.Component;

import java.util.Arrays;

/**
 * @author dzzhyk
 * 对于BeanDefinitionRegistry的前置处理
 */
@Component
public class BeanDefinitionRegistryProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        System.out.println("-------- BeanDefinitionRegistry 处理 - 此处允许添加新的bean定义 --------");
        System.out.println("当前已经拥有的定义：" + Arrays.toString(registry.getBeanDefinitionNames()));
    }
}
