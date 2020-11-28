package com.yankaizhang.spring.webmvc.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件对象实现的接口
 * @author dzzhyk
 * @since 2020-11-28 13:42:05
 */
public interface MultipartFile {

    /**
     * 上传时的指定的文件参数名称
     * @return 返回该文件的参数名
     */
    String getName();

    /**
     * 文件是否为空或者未选择
     * @return 判断结果
     */
    boolean isEmpty();

    /**
     * 原始文件名
     * @return 文件原始名称
     */
    String getOriginalFilename();

    /**
     * 请求的contentType
     * @return 请求的contentType
     */
    String getContentType();

    /**
     * 文件的大小
     * 单位：Byte
     * @return 大小数字
     */
    long getSize();

    /**
     * 以字节数组的形式返回文件内容
     * @return 内容字节数组
     * @throws IOException 异常
     */
    byte[] getBytes() throws IOException;

    /**
     * 返回读取该文件的读入流
     * @return 内容读入流
     * @throws IOException 异常
     */
    InputStream getInputStream() throws IOException;

    /**
     * 将文件复制到另一个目标文件
     * 如果目标文件不存在，则会被创建
     * @param dest 目标文件
     * @throws IOException 异常
     * @throws IllegalStateException 异常
     */
    void transferTo(File dest) throws IOException, IllegalStateException;

}
