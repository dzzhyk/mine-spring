package com.yankaizhang.springframework.beans.factory.support;

import com.yankaizhang.springframework.beans.BeanDefinition;
import com.yankaizhang.springframework.util.ObjectUtils;

import java.util.Objects;

/**
 * 进一步通用的BeanDefinition
 * 实现了{@link com.yankaizhang.springframework.beans.factory.support.AbstractBeanDefinition}抽象类
 * 其实就是在抽象类上添加更多属性和基础实现
 */
public class GenericBeanDefinition extends AbstractBeanDefinition {

    private String parentName;


    public GenericBeanDefinition() {
        super();
    }

    public GenericBeanDefinition(BeanDefinition beanDefinition) {
        super(beanDefinition);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenericBeanDefinition that = (GenericBeanDefinition) o;
        return ObjectUtils.nullSafeEquals(parentName, that.parentName) && super.equals(o);
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentName);
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public String getParentName() {
        return this.parentName;
    }

    @Override
    public String toString() {
        return "GenericBeanDefinition{" +
                "parentName='" + parentName + '\'' +
                '}';
    }
}
