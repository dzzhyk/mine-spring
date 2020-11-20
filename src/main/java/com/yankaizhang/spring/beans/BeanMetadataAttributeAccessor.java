/*
 * Copyright 2002-2017 the original author or authors.
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

package com.yankaizhang.spring.beans;


import com.yankaizhang.spring.core.AttributeAccessorSupport;

/**
 * bean元数据属性访问器
 *
 * 继承了{@link AttributeAccessorSupport}这个抽象类
 * 实现了{@link BeanMetadataElement}这个接口
 *
 * 因此这个类既可以获取元数据，也可以提供属性访问
 * 这里的属性全部都是{@link BeanMetadataAttribute}类型
 * @author dzzhyk
 */

public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {

	private Object source;

	/**
	 * 为这个元数据设置配置来源
	 * @param source 配置来源
	 */
	public void setSource( Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}


	/**
	 * 将给定的BeanMetadataAttribute属性设置到当前对象的元数据属性中
	 * @param attribute Bean对象元属性
	 */
	public void addMetadataAttribute(BeanMetadataAttribute attribute) {
		super.setAttribute(attribute.getName(), attribute);
	}

	/**
	 * 在访问器中查找某一属性
	 * @param name 属性名称
	 * @return 属性对象
	 */
	public BeanMetadataAttribute getMetadataAttribute(String name) {
		return (BeanMetadataAttribute) super.getAttribute(name);
	}

	@Override
	public void setAttribute(String name,  Object value) {
		super.setAttribute(name, new BeanMetadataAttribute(name, value));
	}

	@Override
	public Object getAttribute(String name) {
		BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.getAttribute(name);
		return (attribute != null ? attribute.getValue() : null);
	}

	@Override
	public Object removeAttribute(String name) {
		BeanMetadataAttribute attribute = (BeanMetadataAttribute) super.removeAttribute(name);
		return (attribute != null ? attribute.getValue() : null);
	}

}
