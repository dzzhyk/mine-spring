package com.yankaizhang.spring.beans.factory.impl;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.holder.MutablePropertyValues;
import com.yankaizhang.spring.beans.holder.ConstructorArgumentValues;
import com.yankaizhang.spring.beans.factory.support.AbstractBeanDefinition;

/**
 * RootBeanDefinition是一个实现了AbstractBeanDefinition的实例类
 * Spring内部的Bean一般都使用这个类对象
 * @author dzzhyk
 * @since 2020-11-28 13:52:21
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    public final Object postProcessingLock = new Object();

    /** 标记MergedBeanDefinitionPostProcessor是否已经应用过 */
    public boolean postProcessed = false;

    /** 标记是否已经在实例化之前被解析过 */
    public volatile Boolean beforeInstantiationResolved;

    public RootBeanDefinition() {
        super();
    }

    public RootBeanDefinition(Class<?> beanClass) {
        super();
        setTargetType(beanClass);
    }

    public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck){
        super();
        setTargetType(beanClass);
        setAutowireMode(autowireMode);
        if (dependencyCheck  && getAutowireMode()!=AUTOWIRE_CONSTRUCTOR){
            setDependencyCheck(DEPENDENCY_CHECK_OBJECTS);
        }
    }

    public RootBeanDefinition(Class<?> beanClass,
                              ConstructorArgumentValues constructorArgumentValues,
                              MutablePropertyValues propertyValues) {
        super(constructorArgumentValues, propertyValues);
        setTargetType(beanClass);
    }

    /**
     * 也可以用全类名来初始化RootBeanDefinition
     * 这里不使用类对象而是使用类名，可以避免类class文件总是提前装载
     */
    public RootBeanDefinition (String beanClassName){
        setBeanClass(beanClassName);
    }

    public RootBeanDefinition(String beanClassName,
                              ConstructorArgumentValues constructorArgumentValues,
                              MutablePropertyValues propertyValues) {
        super(constructorArgumentValues, propertyValues);
        setBeanClass(beanClassName);
    }

    /**
     * 从另一个RootBeanDefinition对象创建
     */
    public RootBeanDefinition(RootBeanDefinition other){
        super(other);
        // 另外赋值RootBeanDefinition特有的一些属性

    }

    /**
     * 从另一个BeanDefinition对象创建
     */
    public RootBeanDefinition(BeanDefinition definition){
        super(definition);
    }

    @Override
    public void setParentName(String parentName) throws Exception {
        throw new Exception("系统级别bean定义不能被设置parent");
    }

    /**
     * 系统bean默认返回null
     */
    @Override
    public String getParentName() {
        return null;
    }
}
