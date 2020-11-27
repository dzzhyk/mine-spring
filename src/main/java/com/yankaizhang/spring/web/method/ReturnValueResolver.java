package com.yankaizhang.spring.web.method;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.web.model.ModelAndView;

/**
 * Handler方法返回值处理器解析器的总接口
 * @author dzzhyk
 */
public interface ReturnValueResolver {


    /**
     * 判断该解析器是否可以处理该种返回类型
     * @param parameter 返回参数
     * @return 判断结果
     */
    boolean supportsReturnType(MethodParameter parameter);

    /**
     * 解析返回参数，并且放到{@link ModelAndView}对象中
     * @param returnValue 原始返回值
     * @param returnType 返回类型
     * @param mav ModelAndViewBuilder对象
     * @param webRequest 请求包装类
     * @throws Exception 处理过程中的异常
     */
    void resolveReturnValue(Object returnValue, MethodParameter returnType,
                            ModelAndViewBuilder mav, WebRequest webRequest) throws Exception;

}
