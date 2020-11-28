package com.yankaizhang.spring.webmvc.resolver;

import com.yankaizhang.spring.core.MethodParameter;
import com.yankaizhang.spring.web.http.MediaType;
import com.yankaizhang.spring.web.http.converter.JsonConverter;
import com.yankaizhang.spring.web.method.ArgumentResolver;
import com.yankaizhang.spring.web.method.ReturnValueResolver;
import com.yankaizhang.spring.web.model.ModelAndViewBuilder;
import com.yankaizhang.spring.web.request.WebRequest;
import com.yankaizhang.spring.webmvc.annotation.RequestBody;
import com.yankaizhang.spring.webmvc.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletResponse;

/**
 * 专门处理请求体{@link RequestBody}对象和响应体{@link ResponseBody}对象
 * 参数与返回值解析器
 * @author dzzhyk
 * @since 2020-11-28 13:40:35
 */
public class RequestResponseBodyMethodResolver implements ArgumentResolver, ReturnValueResolver {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    protected JsonConverter jsonConverter;

    public RequestResponseBodyMethodResolver() {
        this.jsonConverter = new JsonConverter();
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, WebRequest webRequest) throws Exception {
        Class<?> parameterType = parameter.getParameterType();
        RequestBody requestBody = parameter.getParameterAnnotation(RequestBody.class);

        String contentType = webRequest.getRequest().getContentType();
        // 得到包装类
        MediaType mediaType = MediaType.getMediaType(contentType);
        if (mediaType == null){
            // 如果没有指定contentType，就尝试用默认的contentType类型
            mediaType = JsonConverter.DEFAULT_CONTENT_TYPE;
        }

        Object result = null;
        if (jsonConverter.canRead(parameterType, mediaType)){
            try {
                result = jsonConverter.read(parameterType, webRequest.getRequest());
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("解析到requestBody失败 => " + parameter.getParameterName()
                        + ", contentType= " + contentType);
            }
        }else{
            if (requestBody.required()){
                throw new Exception("必需的requestBody无法解析 => "
                        + parameter.getParameterName() + ", contentType= " + contentType);
            }
            log.warn("无法解析对应的requestBody => " + parameter.getParameterName() + ", contentType= " + contentType);
        }

        return result;
    }

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ResponseBody.class) ||
                parameter.getMethod().isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public void resolveReturnValue(Object returnValue, MethodParameter returnType,
                                   ModelAndViewBuilder mav, WebRequest webRequest) throws Exception {

        Class<?> parameterType = returnType.getParameterType();

        ServletResponse response = webRequest.getResponse();
        String contentType = response.getContentType();

        MediaType mediaType = MediaType.getMediaType(contentType);
        if (mediaType == null){
            mediaType = JsonConverter.DEFAULT_CONTENT_TYPE;
        }

        if (jsonConverter.canWrite(parameterType, mediaType)){
            jsonConverter.write(returnValue, mediaType, response);
            return;
        }

        log.warn("json返回内容可能解析失败 => " + returnType.toString() + ", contentType= " + contentType);
    }
}
