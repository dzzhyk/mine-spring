package com.yankaizhang.spring.web.view;

import com.yankaizhang.spring.web.View;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 抽象视图类AbstractView，包含了通用操作，实现了{@link View}接口
 * @author dzzhyk
 */
public abstract class AbstractView implements View {

    private String url;

    private String contentType;

    public AbstractView() {}

    public AbstractView(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
