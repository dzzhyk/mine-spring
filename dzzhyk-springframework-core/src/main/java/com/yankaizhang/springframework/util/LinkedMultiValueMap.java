package com.yankaizhang.springframework.util;

import java.io.Serializable;
import java.util.*;


/**
 * 多值Map接口的链表结构实现
 * 不保证线程安全
 */
public class LinkedMultiValueMap<K, V> implements MultiValueMap<K, V>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private final Map<K, List<V>> targetMap;

    // 构造方法

    public LinkedMultiValueMap() {
        this.targetMap = new LinkedHashMap<>();
    }

    public LinkedMultiValueMap(int size){
        this.targetMap = new LinkedHashMap<>(size);
    }

    public LinkedMultiValueMap(Map<K, List<V>> other){
        this.targetMap = new LinkedHashMap<>(other);
    }

    // MultiValueMap方法实现

    @Override
    public V getFirst(K key) {
        List<V> values = this.targetMap.get(key);
        return (null==values || values.isEmpty())? null: values.get(0);
    }

    @Override
    public void add(K key, V value) {
        List<V> vs = this.targetMap.computeIfAbsent(key, k -> new LinkedList<>());
        vs.add(value);
    }

    @Override
    public void addAll(K key, List<? extends V> values) {
        List<V> vs = this.targetMap.computeIfAbsent(key, k -> new LinkedList<>());
        vs.addAll(values);
    }

    @Override
    public void addAll(MultiValueMap<K, V> values) {
        values.forEach(this::addAll);
    }

    @Override
    public void set(K key, V value) {
        List<V> values = new LinkedList<>();
        values.add(value);
        this.targetMap.put(key, values);
    }

    @Override
    public void setAll(Map<K, V> values) {
        values.forEach(this::set);
    }

    @Override
    public Map<K, V> toSingleValueMap() {
        // 获取所以的Key和一个对应的Value
        Map<K, V> singleValueMap = new LinkedHashMap<>(this.targetMap.size());
        this.targetMap.forEach((key, values) -> {
            if (values != null && !values.isEmpty()) {
                singleValueMap.put(key, values.get(0));
            }
        });
        return singleValueMap;
    }

    @Override
    public int size() {
        return this.targetMap.size();
    }


    // Map方法实现

    @Override
    public boolean isEmpty() {
        return this.targetMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.targetMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.targetMap.containsValue(value);
    }

    @Override
    public List<V> get(Object key) {
        return this.targetMap.get(key);
    }

    @Override
    public List<V> put(K key, List<V> value) {
        return this.targetMap.put(key, value);
    }

    @Override
    public List<V> remove(Object key) {
        return this.targetMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends List<V>> m) {
        m.forEach(this::put);
    }

    @Override
    public void clear() {
        this.targetMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return this.targetMap.keySet();
    }

    @Override
    public Collection<List<V>> values() {
        return this.targetMap.values();
    }

    @Override
    public Set<Entry<K, List<V>>> entrySet() {
        return this.targetMap.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedMultiValueMap<?, ?> that = (LinkedMultiValueMap<?, ?>) o;
        return Objects.equals(targetMap, that.targetMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetMap);
    }

    @Override
    public String toString() {
        return "LinkedMultiValueMap{" +
                "targetMap=" + targetMap +
                '}';
    }

    /**
     * 获取深拷贝对象
     */
    public LinkedMultiValueMap<K, V> deepCopy(){
        LinkedMultiValueMap<K, V> copy = new LinkedMultiValueMap<>(size());
        forEach((key, values) -> copy.put(key, new LinkedList<>(values)));
        return copy;
    }

    /**
     * 获取浅拷贝对象
     */
    @Override
    public LinkedMultiValueMap<K, V> clone() {
        return new LinkedMultiValueMap<>(this);
    }
}
