package com.yankaizhang.springframework.util;

import java.util.List;
import java.util.Map;

/**
 * 多值map
 *
 * @author dzzhyk
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {
    /**
     * 返回key对应的值的第一个元素
     */
    V getFirst(K key);

    /**
     * 把元素添加到当前key的values中
     */
    void add(K key, V value);

    /**
     * 添加列表所有的元素到对应key
     */
    void addAll(K key, List<? extends V> values);

    /**
     * 把另一个Map里面的所有内容全部加入到当前的Map
     */
    void addAll(MultiValueMap<K, V> values);

    /**
     * 条件添加
     */
    default void addIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    /**
     * 设置某个key的某个Value值
     */
    void set(K key, V value);

    /**
     * 根据给出的Map设置当前的值
     */
    void setAll(Map<K, V> values);

    /**
     * 返回一个标准Map，每个key对应的value是元素列表中的第一个
     */
    Map<K, V> toSingleValueMap();
}
