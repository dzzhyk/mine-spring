package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Servlet返回值参数解析
 * <li>{@link ServletResponse}
 * <li>{@link OutputStream}
 * <li>{@link Writer}
 * @author dzzhyk
 */
public class ServletResponseMethodArgumentResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> paramType = parameter.getParameterType();
        return (ServletResponse.class.isAssignableFrom(paramType) ||
                OutputStream.class.isAssignableFrom(paramType) ||
                Writer.class.isAssignableFrom(paramType));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();

        if (ServletResponse.class.isAssignableFrom(parameterType)) {
            return webRequest.getResponse();
        } else if (OutputStream.class.isAssignableFrom(parameterType)){
            return webRequest.getResponse().getOutputStream();
        } else if (Writer.class.isAssignableFrom(parameterType)){
            return webRequest.getResponse().getWriter();
        }else{
            throw new Exception("遭遇了不可解析的类型 => " + parameterType.getName());
        }
    }
}
