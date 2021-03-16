package com.yankaizhang.spring.beans;

import com.yankaizhang.spring.beans.holder.PropertyValue;

import java.util.List;

/**
 * 类属性集合的包装类接口
 * @author dzzhyk
 * @since 2020-11-28 13:53:20
 */
public interface PropertyValues {

    /**
     * 获取所有属性值
     * @return 属性值list
     */
    List<PropertyValue> getPropertyValues();


    /**
     * 判断是否包含某个属性
     * @param propertyName 属性名称
     * @return 判断结果
     */
    boolean contains(String propertyName);

    /**
     * 根据名称获取某个属性值
     * @param propertyName 属性名称
     * @return 属性值包装类对象PropertyValue
     */
    PropertyValue getPropertyValue(String propertyName);

    /**
     * 判断属性值是否为空
     * @return 属性值是否为空
     */
    boolean isEmpty();
}
