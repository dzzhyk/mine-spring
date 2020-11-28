package com.yankaizhang.spring.web.http.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yankaizhang.spring.web.http.HttpMessageConverter;
import com.yankaizhang.spring.web.http.MediaType;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析json输入和输出的转换器
 * @author dzzhyk
 * @since 2020-11-28 13:44:59
 */
public class JsonConverter implements HttpMessageConverter<Object> {

    protected ObjectMapper objectMapper = new ObjectMapper();

    /** 当前转换器支持的contentType列表 */
    private static final List<MediaType> MEDIA_TYPES = new ArrayList<>();

    /** 默认的contentType */
    public static final MediaType DEFAULT_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    static {
        // 支持json格式的contentType
        MEDIA_TYPES.add(MediaType.JSON);
        MEDIA_TYPES.add(MediaType.APPLICATION_JSON);
        MEDIA_TYPES.add(MediaType.APPLICATION_JSON_UTF8);
    }

    public JsonConverter() {}

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        return objectMapper.canDeserialize(javaType) && MEDIA_TYPES.contains(mediaType);
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return objectMapper.canSerialize(clazz) && MEDIA_TYPES.contains(mediaType);
    }

    @Override
    public List<MediaType> getSupportedMimeTypes() {
        return MEDIA_TYPES;
    }

    /**
     * 从request中解析出可能的对象
     * @param clazz 数据类型
     * @param request 输入目标
     * @return 解析的对象
     * @throws Exception 解析异常
     */
    @Override
    public Object read(Class<?> clazz, ServletRequest request) throws Exception {

        JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
        Object result = objectMapper.readValue(request.getInputStream(), javaType);

        return result;
    }

    /**
     * 将返回值写回到response中
     * @param value 返回值
     * @param mediaType 返回内容的contentType
     * @param response 返回响应
     * @throws Exception 写回异常
     */
    @Override
    public void write(Object value, MediaType mediaType, ServletResponse response) throws Exception {

        String contentType = mediaType.name();
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);

        String result = objectMapper.writeValueAsString(value);
        response.getWriter().write(result);

    }
}
