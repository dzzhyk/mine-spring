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

package com.yankaizhang.springframework.webmvc.multipart;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传解析器接口
 * CommonsMultipartResolver 是其实现类
 * 其实现使用了apache开源的 commons-fileupload 和 commons-io 包
 * @author dzzhyk
 */
public interface MultipartResolver {

	/**
	 * 检查某个请求是否为文件上传请求
	 * Will typically check for content type "multipart/form-data"
	 */
	boolean isMultipart(HttpServletRequest request);

	/**
	 * 将request包装为一个MultipartHttpServletRequest对象
	 * 其实就是解析过程
	 */
	MultipartRequest resolveMultipart(HttpServletRequest request) throws Exception;

	/**
	 * 清理某文件请求的所有资源内容
	 */
	void cleanupMultipart(MultipartRequest request);

}
