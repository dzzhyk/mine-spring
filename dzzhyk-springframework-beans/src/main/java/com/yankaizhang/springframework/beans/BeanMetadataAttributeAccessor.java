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

package com.yankaizhang.springframework.beans;


import com.yankaizhang.springframework.core.AttributeAccessorSupport;

/**
 * bean元数据属性访问器
 *
 * 继承了{@link com.yankaizhang.springframework.core.AttributeAccessorSupport}这个抽象类
 * 实现了{@link com.yankaizhang.springframework.beans.BeanMetadataElement}这个接口
 *
 * 因此这个类既可以获取元数据，也可以提供属性访问
 * 这里的属性全部都是{@link com.yankaizhang.springframework.beans.BeanMetadataAttribute}类型
 */

@SuppressWarnings("serial")
public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {

	private Object source;

	/**
	 * Set the configuration source {@code Object} for this metadata element.
	 * <p>The exact type of the object will depend on the configuration mechanism used.
	 */
	public void setSource( Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}


	/**
	 * Add the given BeanMetadataAttribute to this accessor's set of attributes.
	 * @param attribute the BeanMetadataAttribute object to register
	 */
	public void addMetadataAttribute(BeanMetadataAttribute attribute) {
		super.setAttribute(attribute.getName(), attribute);
	}

	/**
	 * Look up the given BeanMetadataAttribute in this accessor's set of attributes.
	 * @param name the name of the attribute
	 * @return the corresponding BeanMetadataAttribute object,
	 * or {@code null} if no such attribute defined
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
