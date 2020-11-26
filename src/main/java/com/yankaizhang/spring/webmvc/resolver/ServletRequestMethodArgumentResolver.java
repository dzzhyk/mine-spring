package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Servlet请求参数解析
 * <li>{@link ServletRequest}
 * <li>{@link MultipartRequest}
 * @author dzzhyk
 */
public class ServletRequestMethodArgumentResolver implements ArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return (ServletRequest.class.isAssignableFrom(paramType) ||
                MultipartRequest.class.isAssignableFrom(paramType));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        if (ServletRequest.class.isAssignableFrom(parameterType) || MultipartRequest.class.isAssignableFrom(parameterType)){
            // 如果是request类型
            return webRequest;

        }else{
            throw new Exception("遭遇了不可解析的类型 => " + parameterType.getName());
        }
    }
}
