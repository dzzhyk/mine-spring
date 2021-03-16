package com.yankaizhang.spring.beans.holder;

import com.yankaizhang.spring.beans.PropertyValues;

import java.util.ArrayList;
import java.util.List;


/**
 * 一个Bean对象的属性包装类<br/>
 * 用于在生成BeanDefinition的时候保存该对象的配置属性
 * @author dzzhyk
 * @since 2021-03-08 17:47:01
 */
public class MutablePropertyValues implements PropertyValues {

    private List<PropertyValue> propertyValuesList;

    public MutablePropertyValues() {
        this.propertyValuesList = new ArrayList<>(10);
    }

    /**
     * deep clone
     */
    public MutablePropertyValues(PropertyValues other){
        this();
        if (other != null) {
            List<PropertyValue> propertyValues = other.getPropertyValues();
            this.propertyValuesList = new ArrayList<>(propertyValues.size());
            this.propertyValuesList.addAll(propertyValues);
        }
    }


    public void removePropertyValue(PropertyValue propertyValue){
        this.propertyValuesList.remove(propertyValue);
    }

    public void removePropertyValue(String propertyName){
        this.propertyValuesList.remove(getPropertyValue(propertyName));
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return propertyValuesList;
    }

    @Override
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : this.propertyValuesList) {
            if (propertyValue.getName().equals(propertyName)){
                return propertyValue;
            }
        }
        return null;
    }

    @Override
    public boolean contains(String propertyName){
        return getPropertyValue(propertyName) != null;
    }

    @Override
    public boolean isEmpty() {
        return this.propertyValuesList.isEmpty();
    }

    @Override
    public String toString() {
        return "MutablePropertyValues{" +
                "propertyValuesList=" + propertyValuesList +
                '}';
    }
}
