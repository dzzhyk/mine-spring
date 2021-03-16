package com.yankaizhang.spring.web.http;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * Http参数的策略接口，用于处理<br/>
 * {@link com.yankaizhang.spring.webmvc.annotation.RequestBody}与
 * {@link com.yankaizhang.spring.webmvc.annotation.ResponseBody}<br/>
 * @author dzzhyk
 * @param <T> 模板类型
 * @since 2020-11-28 13:45:05
 */
public interface HttpMessageConverter<T> {

    /**
     * 判断当前转换器是否可以解析传入的数据
     * @param clazz 数据类型
     * @param mediaType 数据的mimeType
     * @return 判断结果
     */
    boolean canRead(Class<?> clazz, MediaType mediaType);

    /**
     * 判断当前转换器是否可以写出传出的数据
     * @param clazz 数据类型
     * @param mediaType 数据的mimeType
     * @return 判断结果
     */
    boolean canWrite(Class<?> clazz, MediaType mediaType);

    /**
     * 获取当前转换器能够解析的所有数据类型
     * @return 数据类型列表
     */
    List<MediaType> getSupportedMimeTypes();

    /**
     * 读取传入的数据
     * @param clazz 数据类型
     * @param request 输入目标
     * @return 读取的数据
     * @throws Exception 处理异常
     */
    T read(Class<? extends T> clazz, ServletRequest request) throws Exception;

    /**
     * 将后台数据转换然后返回给前端
     * @param value 原始值
     * @param contentType 返回响应的contentType
     * @param response 返回响应
     * @throws Exception 处理异常
     */
    void write(T value, MediaType contentType, ServletResponse response) throws Exception;

}
