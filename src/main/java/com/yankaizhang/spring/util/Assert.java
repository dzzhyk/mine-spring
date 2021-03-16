package com.yankaizhang.spring.util;

import java.util.Collection;
import java.util.Map;

/**
 * 断言工具类
 * @author dzzhyk
 * @since 2021-03-16 18:46:07
 */
public class Assert {

    public static void isNull(Object object, String msg) {
        if (null != object){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notNull(Object object, String msg){
        if (null == object){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void isTrue(boolean exp, String msg){
        if (!exp){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(Collection<?> collection, String msg){
        if (CollectionUtils.isEmpty(collection)){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void notEmpty(Map<?, ?> map, String msg){
        if (CollectionUtils.isEmpty(map)){
            throw new IllegalArgumentException(msg);
        }
    }

    public static void hasText(String text, String msg) {
        if (StringUtils.isEmpty(text)){
            throw new IllegalArgumentException(msg);
        }
    }
}
