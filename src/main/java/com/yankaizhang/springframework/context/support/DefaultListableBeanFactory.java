package com.yankaizhang.springframework.context.support;

import com.yankaizhang.springframework.beans.config.BeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * IOC容器子类的一个实现
 * 包含了beanDefinitionMap
 */
public class DefaultListableBeanFactory extends AbstractApplicationContext {

    /**
     * beanDefinitionMap，用来存储注册信息，当做bean定义的缓存
     */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

}
