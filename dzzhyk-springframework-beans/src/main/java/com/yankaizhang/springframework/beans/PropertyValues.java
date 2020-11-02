package com.yankaizhang.springframework.beans;

import java.util.List;

/**
 * 类属性集合的包装类接口
 */
public interface PropertyValues {

    List<PropertyValue> getPropertyValues();

    PropertyValue getPropertyValue(String propertyName);

}
