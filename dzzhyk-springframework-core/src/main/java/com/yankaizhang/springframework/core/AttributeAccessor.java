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

package com.yankaizhang.springframework.core;

/**
 * AttributeAccessor接口定义了元数据MetaData的访问方法
 * @author dzzhyk
 */
public interface AttributeAccessor {

	/**
	 * 设置属性
	 * @param name 属性名称
	 * @param value 属性值
	 */
	void setAttribute(String name,  Object value);

	/**
	 * 获取属性
	 * @param name 属性名称
	 * @return 属性值
	 */
	Object getAttribute(String name);

	/**
	 * 移除属性
	 * @param name 属性名称
	 * @return 移除的属性值
	 */
	Object removeAttribute(String name);

	/**
	 * 判断是否具有指定属性
	 * @param name 属性名
	 * @return 判断结果
	 */
	boolean hasAttribute(String name);

	/**
	 * 返回属性的所有Name
	 * @return 当前所有属性名称列表
	 */
	String[] attributeNames();

}
