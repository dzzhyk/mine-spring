package com.yankaizhang.spring.web.http;

import com.yankaizhang.spring.util.StringUtils;

/**
 * 一些contentType枚举
 * @author dzzhyk
 * @since 2020-11-28 13:45:31
 */
public enum MediaType {

    /** 全部类型 */
    ALL("*/*"),

    /** 文本 */
    TEXT_HTML("text/html"),
    TEXT_MARKDOWN("text/markdown"),
    TEXT_PLAIN("text/plain"),
    TEXT_XML("text/xml"),
    TEXT_CSS("text/css"),
    TEXT_JAVASCRIPT("text/javascript"),

    /** 文件目前只支持form-data格式 */
    MULTIPART_FORM_DATA("multipart/form-data"),
//    MULTIPART_MIXED("multipart/mixed"),
//    MULTIPART_RELATED("multipart/related"),

    /** json字符串 */
    JSON("json"),
    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8"),
    /** 表单 */
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");

    private final String contentType;

    MediaType(String s) {
        this.contentType = s;
    }

    /**
     * 根据contentType字符串获取MediaType对象
     */
    public static MediaType getMediaType(String contentType){
        if (StringUtils.isEmpty(contentType)){
            return null;
        }
        MediaType[] values = MediaType.values();
        for (MediaType value : values) {
            if (value.getContentType().equals(contentType)){
                return value;
            }
        }
        return null;
    }

    public String getContentType() {
        return contentType;
    }
}
