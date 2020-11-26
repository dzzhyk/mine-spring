package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * 专门处理标注了{@link RequestParam}的参数对象
 * @author dzzhyk
 */
public class RequestParamMethodResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest request) {
        RequestParam annotation = parameter.getParameterAnnotation(RequestParam.class);
        String paramName = annotation.value();
        return request.getRequest().getParameter(paramName);
    }
}
