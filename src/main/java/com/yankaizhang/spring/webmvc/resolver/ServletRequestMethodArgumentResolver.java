package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.multipart.MultipartRequest;

import javax.servlet.ServletRequest;

/**
 * Servlet请求参数解析
 * <ul>
 * <li>{@link ServletRequest}
 * <li>{@link MultipartRequest}
 * </ul>
 * @author dzzhyk
 * @since 2020-11-28 13:40:59
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
            return webRequest.getRequest();

        }else{
            throw new Exception("遭遇了不可解析的类型 => " + parameterType.getName());
        }
    }
}
