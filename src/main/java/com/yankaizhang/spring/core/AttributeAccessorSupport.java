/*
 * Copyright 2002-2018 the original author or authors.
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

package com.yankaizhang.spring.core;

import com.yankaizhang.spring.util.StringUtils;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 一个属性访问器的抽象实现，主要就是可以对属性进行增删改查操作的定义
 * Support class for {@link AttributeAccessor AttributeAccessors}, providing
 * a base implementation of all methods. To be extended by subclasses.
 *
 * <p>{@link Serializable} if subclasses and all attribute values are {@link Serializable}.
 * @author dzzhyk
 */

@SuppressWarnings("serial")
public abstract class AttributeAccessorSupport implements AttributeAccessor, Serializable {


	/**
	 * 属性名称-属性值 映射Map
	 */
	private final Map<String, Object> attributes = new LinkedHashMap<>();

	@Override
	public void setAttribute(String name,  Object value) {
		if (value != null) {
			this.attributes.put(name, value);
		}
		else {
			removeAttribute(name);
		}
	}

	@Override
	
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	@Override
	
	public Object removeAttribute(String name) {
		return this.attributes.remove(name);
	}

	@Override
	public boolean hasAttribute(String name) {
		return this.attributes.containsKey(name);
	}

	@Override
	public String[] attributeNames() {
		return StringUtils.toStringArray(this.attributes.keySet());
	}


	/**
	 * 从另一个属性访问器实现拷贝其拥有的所有属性
	 */
	protected void copyAttributesFrom(AttributeAccessor source) {
		String[] attributeNames = source.attributeNames();
		for (String attributeName : attributeNames) {
			setAttribute(attributeName, source.getAttribute(attributeName));
		}
	}


	@Override
	public boolean equals( Object other) {
		return (this == other || (other instanceof AttributeAccessorSupport &&
				this.attributes.equals(((AttributeAccessorSupport) other).attributes)));
	}

	@Override
	public int hashCode() {
		return this.attributes.hashCode();
	}

}
