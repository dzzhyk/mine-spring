package com.yankaizhang.spring.webmvc;

import com.yankaizhang.spring.web.method.HandlerMethod;

import java.util.regex.Pattern;

/**
 * 处理器方法映射包装类
 * @author dzzhyk
 * @since 2020-11-28 13:40:03
 */
public class HandlerMapping {

    private Object controller;

    /** 处理器方法为包装类对象 */
    private HandlerMethod method;

    private Pattern pattern;

    public HandlerMapping(Object controller, HandlerMethod method, Pattern pattern) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public HandlerMethod getMethod() {
        return method;
    }

    public void setMethod(HandlerMethod method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
