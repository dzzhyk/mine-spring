package com.yankaizhang.spring.web.request;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 请求包装类，其实同时记录了请求和响应两个信息
 * @author dzzhyk
 * @since 2020-11-28 13:43:58
 */
public class WebRequest {

    private ServletRequest request;

    private ServletResponse response;

    public WebRequest(ServletRequest request, ServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public ServletRequest getRequest() {
        return request;
    }

    public void setRequest(ServletRequest request) {
        this.request = request;
    }

    public ServletResponse getResponse() {
        return response;
    }

    public void setResponse(ServletResponse response) {
        this.response = response;
    }
}
