package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.BeanUtils;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.RequestParam;

/**
 * 专门解析controller的普通参数
 * @author dzzhyk
 */
public class SimpleClassMethodResolver implements ArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 没有@RequestParam标注的简单参数
        return BeanUtils.isSimpleValueType(parameter.getParameterType()) &&
                !parameter.hasParameterAnnotation(RequestParam.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();

        if (parameterType.isInterface()){
            return null;
        }

        Object result;
        try {
            result = parameterType.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new Exception("创建 " + parameterType.getName() + " 类型的方法参数失败 => ");
        }
        return result;
    }

}
