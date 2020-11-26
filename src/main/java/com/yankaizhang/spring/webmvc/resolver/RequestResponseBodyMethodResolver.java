package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.ModelAndView;
import com.yankaizhang.spring.webmvc.annotation.RequestBody;
import com.yankaizhang.spring.webmvc.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 专门处理请求体{@link RequestBody}对象和响应体{@link ResponseBody}对象
 * 参数与返回值解析器
 * @author dzzhyk
 */
public class RequestResponseBodyMethodResolver implements ArgumentResolver, ReturnValueResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) {
        return null;
    }

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ResponseBody.class);
    }

    @Override
    public void resolveReturnValue(Object returnValue, MethodParameter returnType, ModelAndView mav, WebRequest webRequest) throws Exception {

    }
}
