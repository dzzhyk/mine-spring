/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.yankaizhang.springframework.beans.factory.annotation;


import com.yankaizhang.springframework.beans.factory.support.GenericBeanDefinition;
import com.yankaizhang.springframework.core.type.AnnotationMetadata;
import com.yankaizhang.springframework.core.type.MethodMetadata;

/**
 * {@link com.yankaizhang.springframework.beans.factory.support.GenericBeanDefinition}的注解拓展对象
 * 通过实现AnnotatedBeanDefinition接口拥有了注解元数据访问的能力
 */

@SuppressWarnings("serial")
public class AnnotatedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {

	/**
	 * 注解元数据
	 */
	private final AnnotationMetadata metadata;
	/**
	 * 工厂方法元数据
	 */
	private MethodMetadata factoryMethodMetadata;


	/**
	 * 为指定的类对象创建一个AnnotatedGenericBeanDefinition
	 */
	public AnnotatedGenericBeanDefinition(Class<?> beanClass) {
		setBeanClass(beanClass);
		this.metadata = AnnotationMetadata.introspect(beanClass);
	}

	/**
	 * 给定注解元数据，创建一个AnnotatedGenericBeanDefinition
	 */
	public AnnotatedGenericBeanDefinition(AnnotationMetadata metadata) {
		setBeanClassName(metadata.getClassName());
		this.metadata = metadata;
	}

	/**
	 * 更定注解元数据和工厂方法元数据创建对象
	 */
	public AnnotatedGenericBeanDefinition(AnnotationMetadata metadata, MethodMetadata factoryMethodMetadata) {
		this(metadata);
		setFactoryMethodName(factoryMethodMetadata.getMethodName());
		this.factoryMethodMetadata = factoryMethodMetadata;
	}


	@Override
	public final AnnotationMetadata getMetadata() {
		return this.metadata;
	}

	@Override
	public final MethodMetadata getFactoryMethodMetadata() {
		return this.factoryMethodMetadata;
	}

}
