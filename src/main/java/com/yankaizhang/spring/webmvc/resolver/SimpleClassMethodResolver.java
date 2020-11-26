package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.BeanUtils;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 专门解析controller的普通参数
 * @author dzzhyk
 */
public class SimpleClassMethodResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return BeanUtils.isSimpleValueType(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        Object result;
        try {
            result = parameterType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Exception("创建 " + parameterType.getName() + " 类型的方法参数失败 => ");
        }
        return result;
    }

}
