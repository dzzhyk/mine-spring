package com.yankaizhang.springframework.webmvc.multipart.support;

import com.yankaizhang.springframework.util.LinkedMultiValueMap;
import com.yankaizhang.springframework.util.MultiValueMap;
import com.yankaizhang.springframework.webmvc.multipart.MultipartFile;
import com.yankaizhang.springframework.webmvc.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequestWrapper;

/**
 * MultipartRequest接口的抽象实现类，主要是提取了一些对于上传文件对象的公共操作
 * 提供对预生成的MultipartFile实例的管理
 * @author dzzhyk
 */
public abstract class AbstractMultipartRequest extends HttpServletRequestWrapper
        implements MultipartRequest {

    /**
     * 多值文件Map
     * 前面是文件的参数名称类型为String
     * 后面是文件对象，可能有多个
     */
    private MultiValueMap<String, MultipartFile> multipartFiles;

    protected AbstractMultipartRequest(HttpServletRequest request) {
        super(request);
    }


    @Override
    public HttpServletRequest getRequest() {
        return (HttpServletRequest) super.getRequest();
    }


    @Override
    public Iterator<String> getFileNames() {
        return getMultipartFiles().keySet().iterator();
    }

    @Override
    public MultipartFile getFile(String name) {
        return getMultipartFiles().getFirst(name);
    }

    public List<MultipartFile> getFiles(String name) {
        List<MultipartFile> multipartFiles = getMultipartFiles().get(name);
        if (multipartFiles != null) {
            return multipartFiles;
        }
        else {
            return Collections.emptyList();
        }
    }

    @Override
    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return getMultipartFiles();
    }

    /**
     * 判断这个文件上传请求的处理情况
     * @return 是否已经处理了这个文件请求
     */
    public boolean isResolved() {
        return (this.multipartFiles != null);
    }


    /**
     * 设置文件对象
     */
    protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles) {
        this.multipartFiles =
                new LinkedMultiValueMap<>(Collections.unmodifiableMap(multipartFiles));
    }

    /**
     * 获取可以遍历的文件Map
     */
    protected MultiValueMap<String, MultipartFile> getMultipartFiles() {
        if (this.multipartFiles == null) {
            this.multipartFiles = new LinkedMultiValueMap<>();
        }
        return this.multipartFiles;
    }


}
