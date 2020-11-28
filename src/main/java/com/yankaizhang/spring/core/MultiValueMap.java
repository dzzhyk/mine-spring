package com.yankaizhang.spring.core;

import java.util.List;
import java.util.Map;

/**
 * 多值map
 * @author dzzhyk
 * @since 2020-11-28 13:47:43
 */
public interface MultiValueMap<K, V> extends Map<K, List<V>> {
    /**
     * 返回key对应的值的第一个元素
     * @param key key值
     * @return 元素对象
     */
    V getFirst(K key);

    /**
     * 把元素添加到当前key的values中
     * @param key key值
     * @param value value值
     */
    void add(K key, V value);

    /**
     * 添加列表所有的元素到对应key
     * @param key key值
     * @param values value值集合
     */
    void addAll(K key, List<? extends V> values);

    /**
     * 把另一个Map里面的所有内容全部加入到当前的Map
     * @param values 另一个MultiValueMap
     */
    void addAll(MultiValueMap<K, V> values);

    /**
     * 条件添加
     * @param key key值
     * @param value value值
     */
    default void addIfAbsent(K key, V value) {
        if (!containsKey(key)) {
            add(key, value);
        }
    }

    /**
     * 设置某个key的某个Value值
     * @param key key值
     * @param value value值
     */
    void set(K key, V value);

    /**
     * 根据给出的Map设置当前的值
     * @param values 给出的map
     */
    void setAll(Map<K, V> values);

    /**
     * 返回一个标准Map，每个key对应的value是元素列表中的第一个
     * @return 返回的Map
     */
    Map<K, V> toSingleValueMap();
}
