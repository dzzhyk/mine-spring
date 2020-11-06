package com.yankaizhang.springframework.webmvc.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 上传文件对象实现的接口
 */
public interface MultipartFile {

    /**
     * 上传时的指定的文件参数名称
     */
    String getName();

    /**
     * 文件是否为空或者未选择
     */
    boolean isEmpty();

    /**
     * 原始文件名
     */
    String getOriginalFilename();

    /**
     * 文件的contentType
     */
    String getContentType();

    /**
     * 文件的大小
     * 单位：Byte
     */
    long getSize();

    /**
     * 以字节数组的形式返回文件内容
     */
    byte[] getBytes() throws IOException;

    /**
     * 返回读取该文件的读入流
     */
    InputStream getInputStream() throws IOException;

    /**
     * 将文件复制到另一个目标文件
     * 如果目标文件不存在，则会被创建
     */
    void transferTo(File dest) throws IOException, IllegalStateException;


}
