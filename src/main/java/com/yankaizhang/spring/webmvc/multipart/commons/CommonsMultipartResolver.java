package com.yankaizhang.spring.webmvc.multipart.commons;

import com.yankaizhang.spring.webmvc.context.ServletContextAware;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;
import com.yankaizhang.spring.webmvc.multipart.MultipartResolver;
import com.yankaizhang.spring.webmvc.multipart.support.AbstractMultipartRequest;
import com.yankaizhang.spring.webmvc.multipart.support.DefaultMultipartRequest;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @author dzzhyk
 * @since 2020-11-28 13:41:33
 */

public class CommonsMultipartResolver extends CommonsFileUploadSupport
		implements MultipartResolver, ServletContextAware {

	/**
	 * web容器的临时文件暂存路径
	 */
	private static final String TEMP_DIR = "javax.servlet.context.tempdir";


	public CommonsMultipartResolver() {
		super();
	}

	/**
	 * 设置容器上下文，这里重写了原方法，设置了文件的暂存路径
	 * @param context web容器上下文对象
	 */
	@Override
	public void setServletContext(ServletContext context) {
		File attribute = (File) context.getAttribute(TEMP_DIR);
		getFileItemFactory().setRepository(attribute);
	}

	@Override
	public boolean isMultipart(HttpServletRequest request) {
		return ServletFileUpload.isMultipartContent(request);
	}

	@Override
	public MultipartRequest resolveMultipart(HttpServletRequest request) throws Exception {
		if (null == request){
			throw new Exception("请求request不能为null");
		}
		MultipartParsingResult parsingResult = parseRequest(request);
		return new DefaultMultipartRequest(request, parsingResult.getMultipartFiles(),
				parsingResult.getMultipartParameters(), parsingResult.getMultipartParameterContentTypes());
	}

	/**
	 * 解析request
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
				logger.warn("清除文件信息失败！", ex);
			}
		}
	}

	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		return new ServletFileUpload(fileItemFactory);
	}
}
