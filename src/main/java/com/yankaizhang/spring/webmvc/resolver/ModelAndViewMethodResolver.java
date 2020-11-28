package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.web.model.ModelAndView;

/**
 * 专门用来处理{@link ModelAndView}对象的参数与返回值处理器
 * @author dzzhyk
 * @since 2020-11-28 13:40:35
 */
@SuppressWarnings("all")
public class ModelAndViewMethodResolver implements ArgumentResolver, ReturnValueResolver {


    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest request) {
        // 如果是ModelAndView，直接返回一个新的即可
        return new ModelAndView();
    }

    @Override
    public void resolveReturnValue(Object returnValue, MethodParameter returnType,
                                   ModelAndViewBuilder mav, WebRequest request) throws Exception {
        if (returnValue == null) {
            return;
        }
        else if (returnValue instanceof ModelAndView) {
            // 目前把返回值的mav直接赋值给结果mav
            mav.from((ModelAndView) returnValue);
            mav.setCleared(false);
        }
        else {
            throw new Exception("遭遇了不可解析的类型 => " + returnType.getParameterType().getName());
        }
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ModelAndView.class == parameter.getParameterType();
    }

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return ModelAndView.class == parameter.getParameterType();
    }

}
