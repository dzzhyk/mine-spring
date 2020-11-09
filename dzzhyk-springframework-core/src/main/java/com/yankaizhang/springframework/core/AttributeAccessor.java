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
 */
public interface AttributeAccessor {

	void setAttribute(String name,  Object value);

	Object getAttribute(String name);
	
	Object removeAttribute(String name);

	boolean hasAttribute(String name);

	/**
	 * 返回属性的所有Name
	 */
	String[] attributeNames();

}
