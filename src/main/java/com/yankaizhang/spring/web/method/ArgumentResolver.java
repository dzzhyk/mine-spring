package com.yankaizhang.spring.web.method;


import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.request.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Handler方法参数解析器的总接口
 * @author dzzhyk
 */
public interface ArgumentResolver {

    /**
     * 判断该解析器是否支持该类型方法参数的参数解析
     * @param parameter 方法参数包装类
     * @return 判断结果
     */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 从给定的{@link HttpServletRequest}对象参数中解析得到，给定的{@link MethodParameter}对象
     * @param parameter 指定的方法参数对象
     * @param webRequest 请求包装类
     * @throws Exception 解析过程中的请求
     * @return 解析结果
     */
    Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception;
}
