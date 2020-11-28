package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.ResponseBody;


/**
 * 用于解析{@code void}类型与所有{@link CharSequence}的实现类的返回值解析器
 * @author dzzhyk
 * @since 2020-11-28 13:41:14
 */
public class ViewNameMethodReturnValueResolver implements ReturnValueResolver {


    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();

        // 需要排除返回体@ResponseBody标注的内容
        return (void.class== parameterType || CharSequence.class.isAssignableFrom(parameterType)) &&
                !parameter.getMethod().isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public void resolveReturnValue(Object returnValue, MethodParameter returnType,
                                   ModelAndViewBuilder mav, WebRequest webRequest) throws Exception {
        if (returnValue instanceof CharSequence) {
            String viewName = returnValue.toString();
            mav.setViewName(viewName);
            mav.setCleared(false);
        }
        else if (returnValue != null) {
            throw new Exception("遭遇了不可解析的类型 => " + returnType.getParameterType().getName());
        }

        // 如果是null就什么都不做
    }
}
