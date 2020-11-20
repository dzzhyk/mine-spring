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

/**
 * bean定义的某个元数据属性的包装类
 * @author dzzhyk
 */
public class BeanMetadataAttribute implements BeanMetadataElement {

	private final String name;

	private final Object value;

	private Object source;


	public BeanMetadataAttribute(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	/**
	 * 设置设个数据的产生源头
	 */
	public void setSource( Object source) {
		this.source = source;
	}

	@Override
	public Object getSource() {
		return this.source;
	}

}
