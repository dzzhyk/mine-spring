package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.util.ClassUtils;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.RequestParam;
import com.yankaizhang.spring.webmvc.multipart.support.DefaultMultipartRequest;
import com.yankaizhang.spring.webmvc.multipart.support.MultiPartUtils;

import javax.servlet.http.HttpServletRequest;


/**
 * 专门处理标注了{@link RequestParam}的参数对象
 * @author dzzhyk
 */
public class RequestParamMethodArgumentResolver implements ArgumentResolver {



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(RequestParam.class) || MultiPartUtils.isMultipartArgument(parameter));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) {

        RequestParam annotation = parameter.getParameterAnnotation(RequestParam.class);
        String paramName = annotation.value();

        if (MultiPartUtils.isMultipartArgument(parameter)){

            // 如果是文件请求，应该是已经被解析过了

            DefaultMultipartRequest multipartRequest =
                    new DefaultMultipartRequest((HttpServletRequest) webRequest.getRequest());
            return multipartRequest.getFile(paramName);

        } else {
            String result = webRequest.getRequest().getParameter(paramName);
            Class<?> parameterType = parameter.getParameterType();

            // 尝试将String类型转换为需求的参数类型
            Object classObject = getParameterObject(result, parameterType);
            return classObject;
        }
    }


    /**
     * 尝试将当前对象某个参数类型的对象
     */
    public Object getParameterObject(String string, Class<?> clazz) {

        if (CharSequence.class.isAssignableFrom(clazz)){
            return string;
        }

        if (int.class == clazz || Integer.class == clazz) {
            return Integer.parseInt(string);
        } else if (double.class == clazz || Double.class == clazz) {
            return Double.parseDouble(string);
        } else if (char.class == clazz || Character.class == clazz){
            return string.toCharArray()[0];
        } else if (boolean.class == clazz || Boolean.class == clazz){
            return Boolean.parseBoolean(string);
        } else if (short.class == clazz || Short.class == clazz){
            return Short.parseShort(string);
        } else if (float.class == clazz || Float.class == clazz){
            return Float.parseFloat(string);
        } else if (byte.class == clazz || Byte.class == clazz){
            return Byte.parseByte(string);
        }
        return null;
    }
}
