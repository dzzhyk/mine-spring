/*
 * Copyright 2002-2004 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */ 

package com.yankaizhang.spring.webmvc.multipart;

import com.yankaizhang.spring.core.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
 * 实现这个接口代表是一个文件上传请求request
 * DefaultMultipartRequest是这个接口的具体实现类
 * @author dzzhyk
 * @since 2020-11-28 13:42:09
 */
public interface MultipartRequest extends HttpServletRequest {

	/**
	 * 返回一个迭代器
	 * 这个迭代器的迭代对象是当前请求上传文件的所有属性对应name
	 * 比如：file1, file2, file3
	 * 而不是具体的文件名称 1.jpg, 2.jpg, 3.jpg
	 *
	 * @return 所有上传文件name的迭代器
	 */
	Iterator<String> getFileNames();

	/**
	 * 返回文件包装类或者null
	 * @param name 请求中该文件的name
	 * @return 文件包装类
	 */
	MultipartFile getFile(String name);

	/**
	 * 返回请求的所有文件Map
	 * @return Multipart文件name和对象映射Map
	 */
	MultiValueMap<String, MultipartFile> getMultiFileMap();
}
