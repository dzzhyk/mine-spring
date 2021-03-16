package com.yankaizhang.spring.beans.factory.config;

import com.yankaizhang.spring.beans.factory.generic.GenericBeanDefinition;

/**
 * 这个处理器接口用于在执行所有前置处理器之前，预处理MergedBeanDefinition<br/>
 * Autowired注解使用该bean后置处理器方式注入
 * 这个注解的实现类
 * @author dzzhyk
 * @since 2021-03-08 17:04:34
 */
public interface MergedBeanDefinitionPostProcessor extends BeanPostProcessor {
	
	/**
	 * 预处理MergedBeanDefinition
	 * @param beanDefinition bean定义
	 * @param beanType bean类型
	 * @param beanName bean名称
	 */
	void postProcessMergedBeanDefinition(GenericBeanDefinition beanDefinition, Class<?> beanType, String beanName);

}