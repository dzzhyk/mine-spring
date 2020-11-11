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
 * MultipartRequest接口的抽象实现类
 * 提供对预生成的MultipartFile实例的管理
 * @author dzzhyk
 */
public abstract class AbstractMultipartRequest extends HttpServletRequestWrapper
        implements MultipartRequest {

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

    public MultiValueMap<String, MultipartFile> getMultiFileMap() {
        return getMultipartFiles();
    }

    /**
     * Determine whether the underlying multipart request has been resolved.
     * @return {@code true} when eagerly initialized or lazily triggered,
     * {@code false} in case of a lazy-resolution request that got aborted
     * before any parameters or multipart files have been accessed
     * @since 4.3.15
     * @see #getMultipartFiles()
     */
    public boolean isResolved() {
        return (this.multipartFiles != null);
    }


    /**
     * Set a Map with parameter names as keys and list of MultipartFile objects as values.
     * To be invoked by subclasses on initialization.
     */
    protected final void setMultipartFiles(MultiValueMap<String, MultipartFile> multipartFiles) {
        this.multipartFiles =
                new LinkedMultiValueMap<>(Collections.unmodifiableMap(multipartFiles));
    }

    /**
     * Obtain the MultipartFile Map for retrieval
     */
    protected MultiValueMap<String, MultipartFile> getMultipartFiles() {
        if (this.multipartFiles == null) {
            this.multipartFiles = new LinkedMultiValueMap<>();
        }
        return this.multipartFiles;
    }


}
