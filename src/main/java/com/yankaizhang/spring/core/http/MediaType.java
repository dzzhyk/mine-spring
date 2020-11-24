package com.yankaizhang.spring.core.http;

/**
 * 一些mime-type
 * @author dzzhyk
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
    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_UTF8("application/json;charset=UTF-8"),
    /** 表单 */
    APPLICATION_FORM_URLENCODED("application/x-www-form-urlencoded");


    MediaType(String s) {

    }

    /**
     * 根据contentType字符串获取枚举对象
     */
    MediaType getContentType(String contentType){
        return MediaType.valueOf(contentType);
    }
}
