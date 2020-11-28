package com.yankaizhang.spring.web.http;

import java.util.HashMap;
import java.util.Map;

/**
 * Http请求方法枚举类
 * @author dzzhyk
 * @since 2020-11-28 13:45:26
 */
@SuppressWarnings("all")
public enum HttpMethod {

    GET, POST, DELETE, PUT, OPTIONS, PATCH, TRACE;

    private static final Map<String, HttpMethod> mappings;

    static {
        mappings = new HashMap<>(16);
        for (HttpMethod value : values()) {
            mappings.put(value.name(), value);
        }
    }

    /**
     * 根据request的请求方法名解析得到枚举类对象
     * @param method 请求方法字符串
     * @return 枚举类对象
     */
    public static HttpMethod resolve(String method) {
        return (method != null ? mappings.get(method) : null);
    }

    /**
     * 判断某个方法是否为Http请求方法之一
     * @param method 请求方法字符串
     * @return 判断结果
     */
    public boolean matches(String method) {
        return (this == resolve(method));
    }
}
