package com.yankaizhang.spring.webmvc.multipart.support;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.webmvc.multipart.MultipartFile;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.Collection;
import java.util.List;

/**
 * 上传请求工具类
 * @author dzzhyk
 */
public class MultiPartUtils {

    /**
     * 判断某个请求是否为MultiPart类型
     */
    public static boolean isMultiPartRequest(HttpServletRequest request){
        return (request instanceof MultipartRequest && isMultipartContent(request));
    }

    /**
     * 判断某个请求是否为多值类型
     */
    private static boolean isMultipartContent(HttpServletRequest request) {
        String contentType = request.getContentType();
        return (contentType != null && contentType.toLowerCase().startsWith("multipart/"));
    }

    /**
     * 判断某个{@link MethodParameter}对象是否为文件类型参数
     */
    public static boolean isMultipartArgument(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return MultipartFile.class == paramType;
    }

}
