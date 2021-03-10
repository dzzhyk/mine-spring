package com.yankaizhang.spring.beans.holder;


import com.yankaizhang.spring.beans.BeanMetadataElement;
import com.yankaizhang.spring.util.ObjectUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 构造函数的参数列表包装类
 * @author dzzhyk
 * @since 2020-12-02 14:55:16
 */
public class ConstructorArgumentValues {

    /**
     * 参数位置和参数对照
     */
    private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<>();
    /**
     * 通用的参数列表
     */
    private final List<ValueHolder> genericArgumentValues = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public boolean isEmpty() {
        return (this.indexedArgumentValues.isEmpty() && this.genericArgumentValues.isEmpty());
    }


    //TODO: 构造函数的属性增删改查功能还需要添加



    public Map<Integer, ValueHolder> getIndexedArgumentValues() {
        return indexedArgumentValues;
    }

    public List<ValueHolder> getGenericArgumentValues() {
        return genericArgumentValues;
    }

    /**
     * 构造函数的参数值的包装类
     * 实现了BeanMetadataElement表明他也拥有一个来源，并且可以获取这个来源
     */
    private class ValueHolder implements BeanMetadataElement {

        private String name;    // 这个参数名称
        private Object value;   // 参数值 （必须有）
        private String type;    // 参数全类名
        private Object source;  // 这个参数的配置来源
        private boolean converted;      // 是否需要转换
        private Object convertedValue;  // 转换后的值

        public ValueHolder(Object value) {
            this.value = value;
        }

        public ValueHolder(Object value, String type) {
            this.value = value;
            this.type = type;
        }

        public ValueHolder(Object value, String type, String name) {
            this.value = value;
            this.type = type;
            this.name = name;
        }

        /**
         * 比较属性是否相同
         * 这里没有实现equals方法，因为如果使用Set包裹ValueHolder，这样就可以保证相同的内容也是不同的元素
         * 一个方法的参数可能会传入多次？这些都是需要区分的
         */
        private boolean contentEquals(ValueHolder other){
            return (this==other ||
                    (ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, this.type)));
        }


        /**
         * 这里实现对于内容的简易哈希，为了保证在Set里面也是可以区分的
         */
        private int contentHashCode(){
            return this.value.hashCode() * 29 + this.type.hashCode();
        }

        /**
         * 深拷贝
         */
        public ValueHolder copy() {
            ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
            copy.setSource(this.source);
            return copy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public boolean isConverted() {
            return converted;
        }

        public void setConverted(boolean converted) {
            this.converted = converted;
        }

        public Object getConvertedValue() {
            return convertedValue;
        }

        public void setConvertedValue(Object convertedValue) {
            this.convertedValue = convertedValue;
        }
    }
}
