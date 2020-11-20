package com.yankaizhang.spring.beans;

import java.util.List;

/**
 * 类属性集合的包装类接口
 * @author dzzhyk
 */
public interface PropertyValues {

    /**
     * 获取所有属性值
     * @return 属性值list
     */
    List<PropertyValue> getPropertyValues();

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
