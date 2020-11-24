package com.yankaizhang.spring.core.http;

import java.util.List;

/**
 * Http参数的策略接口，用于处理
 * {@link com.yankaizhang.spring.webmvc.annotation.RequestBody}与
 * {@link com.yankaizhang.spring.webmvc.annotation.ResponseBody}
 * @author dzzhyk
 * @param <T>
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
     * @param inputMessage 输入数据
     * @return 读取的数据
     * @throws Exception 抛出的简化异常
     */
    T read(Class<? extends T> clazz, HttpInputMessage inputMessage) throws Exception;

    /**
     * 将后台数据转换然后返回给前端
     * @param t
     * @param contentType
     * @param outputMessage
     * @throws Exception
     */
    void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws Exception;

}
