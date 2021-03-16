package com.yankaizhang.spring.beans.factory.generic;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.support.AbstractBeanDefinition;
import com.yankaizhang.spring.util.ObjectUtils;

import java.util.Objects;

/**
 * 通用的BeanDefinition<br/>
 * 实现了{@link AbstractBeanDefinition}抽象类<br/>
 * 添加了父bean定义属性<br/>
 *<br/>
 * 一般定义的BeanDefinition对象都是这个实现类<br/>
 * @author dzzhyk
 * @since 2021-03-13 16:02:19
 */
public class GenericBeanDefinition extends AbstractBeanDefinition {

    private String parentName;

    public Object postProcessingLock = new Object();
    public boolean postProcessed = false;

    public GenericBeanDefinition() {
        super();
    }

    public GenericBeanDefinition(BeanDefinition beanDefinition) {
        super(beanDefinition);
    }

    public GenericBeanDefinition(Class<?> beanClass){
        super();
        setBeanClass(beanClass);
        setBeanClassName(beanClass.getName());
    }

    public GenericBeanDefinition(Class<?> beanClass, int autowireMode, int dependencyCheck){
        super();
        setBeanClass(beanClass);
        setAutowireMode(autowireMode);
        setDependencyCheck(dependencyCheck);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
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
                "beanClass='" + getBeanClassName() + '\'' +
                "} ";
    }
}
