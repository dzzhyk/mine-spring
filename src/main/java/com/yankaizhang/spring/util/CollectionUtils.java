package com.yankaizhang.spring.util;

import java.util.Collection;
import java.util.Map;

/**
 * 数组类工具类
 * @author dzzhyk
 * @since 2020-11-28 13:45:58
 */
public class CollectionUtils {


    /**
     * 判断某个集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * 判断某个map是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map == null || map.isEmpty());
    }


    /**
     * 判断某个集合不为空
     */
    public static boolean notEmpty(Collection<?> collection){
        return !isEmpty(collection);
    }

    /**
     * 判断某个map不为空
     */
    public static boolean notEmpty(Map<?, ?> map){
        return !isEmpty(map);
    }
}
