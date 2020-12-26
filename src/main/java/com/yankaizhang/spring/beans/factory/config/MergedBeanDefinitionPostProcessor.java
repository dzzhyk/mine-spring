package com.yankaizhang.spring.beans.factory.config;

import com.yankaizhang.spring.beans.factory.impl.RootBeanDefinition;

/**
 * 这个处理器接口用于在执行所有前置处理器之前，预处理MergedBeanDefinition
 * Java配置类的解析就是利用该接口进行的
 * @author dzzhyk
 * @since 2020-12-26 01:39:14
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
	
	/**
	 * 预处理MergedBeanDefinition
	 * @param beanDefinition bean定义
	 * @param beanType bean类型
	 * @param beanName bean名称
	 */
	void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName);

}