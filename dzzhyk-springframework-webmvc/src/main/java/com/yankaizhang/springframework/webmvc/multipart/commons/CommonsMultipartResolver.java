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

package com.yankaizhang.springframework.webmvc.multipart.commons;

import com.yankaizhang.springframework.webmvc.context.ServletContextAware;
import com.yankaizhang.springframework.webmvc.multipart.MultipartRequest;
import com.yankaizhang.springframework.webmvc.multipart.MultipartResolver;
import com.yankaizhang.springframework.webmvc.multipart.support.AbstractMultipartRequest;
import com.yankaizhang.springframework.webmvc.multipart.support.DefaultMultipartRequest;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * 真正使用到的MultipartResolver
 *
 * 提供bean属性的maximumFileSize，maximumInMemorySize和headerEncoding设置
 * 上传中的临时文件保存在服务器的临时目录下
 * 需要使用web IoC容器创建它，这里简化为不能独立存在
 */
public class CommonsMultipartResolver extends CommonsFileUploadSupport
		implements MultipartResolver, ServletContextAware {

	public static final Logger log = LoggerFactory.getLogger(CommonsMultipartResolver.class);

	public CommonsMultipartResolver() {
		super();
	}

	@Override
	public void setServletContext(ServletContext context) {
		File attribute = (File) context.getAttribute("javax.servlet.context.tempdir");
		getFileItemFactory().setRepository(attribute);
	}

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

	@Override
	public MultipartRequest resolveMultipart(HttpServletRequest request) throws Exception {
		Assert.notNull(request, "请求request不能为null");
		MultipartParsingResult parsingResult = parseRequest(request);
		return new DefaultMultipartRequest(request, parsingResult.getMultipartFiles(),
				parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
	}

	/**
	 * 解析request
	 *
	 */
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws Exception {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = getFileUpload();
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			return parseFileItems(fileItems, encoding);
		}
		catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new Exception("总上传大小超出限制", ex);
		}
		catch (FileUploadBase.FileSizeLimitExceededException ex) {
			throw new Exception("单个文件大小超出限制", ex);
		}
		catch (FileUploadException ex) {
			throw new Exception("解析文件上传request失败", ex);
		}
	}

	protected String determineEncoding(HttpServletRequest request) {
		String encoding = request.getCharacterEncoding();
		if (encoding == null) {
			encoding = getDefaultEncoding();
		}
		return encoding;
	}

	@Override
	public void cleanupMultipart(MultipartRequest request) {
		if (!(request instanceof AbstractMultipartRequest) ||
				((AbstractMultipartRequest) request).isResolved()) {
			try {
				cleanupFileItems(request.getMultiFileMap());
			}
			catch (Throwable ex) {
				logger.warn("Failed to perform multipart cleanup for servlet request", ex);
			}
		}
	}

	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		return new ServletFileUpload(fileItemFactory);
	}
}
