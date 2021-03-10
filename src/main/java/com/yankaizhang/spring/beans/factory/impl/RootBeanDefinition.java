package com.yankaizhang.spring.beans.factory.impl;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.generic.GenericBeanDefinition;
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
        setBeanClass(beanClass);
    }

    public RootBeanDefinition(Class<?> beanClass, int autowireMode, boolean dependencyCheck){
        super();
        setBeanClass(beanClass);
        setAutowireMode(autowireMode);
        if (dependencyCheck  && getAutowireMode()!=AUTOWIRE_CONSTRUCTOR){
            setDependencyCheck(DEPENDENCY_CHECK_OBJECTS);
        }
    }

    public RootBeanDefinition(Class<?> beanClass,
                              ConstructorArgumentValues constructorArgumentValues,
                              MutablePropertyValues propertyValues) {
        super(constructorArgumentValues, propertyValues);
        setBeanClass(beanClass);
    }

    /**
     * 也可以用全类名来初始化RootBeanDefinition
     * 这里不使用类对象而是使用类名，可以避免类class文件总是提前装载
     */
    public RootBeanDefinition (String beanClassName){
        setBeanClassName(beanClassName);
    }

    public RootBeanDefinition(String beanClassName,
                              ConstructorArgumentValues constructorArgumentValues,
                              MutablePropertyValues propertyValues) {
        super(constructorArgumentValues, propertyValues);
        setBeanClassName(beanClassName);
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
    public RootBeanDefinition(GenericBeanDefinition definition){
        super(definition);
        try {
            this.setBeanClassName(definition.getBeanClassName());
            this.setBeanClass(definition.getBeanClass());
            this.setLazyInit(definition.isLazyInit());
            this.setInitMethodName(definition.getInitMethodName());
            this.setDestroyMethodName(definition.getDestroyMethodName());
            this.setFactoryBeanName(definition.getFactoryBeanName());
            this.setFactoryMethodName(definition.getFactoryMethodName());
            this.setAutowireMode(definition.getAutowireMode());
            this.setParentName(definition.getParentName());
            this.setAbstractFlag(definition.isAbstractFlag());
            this.setScope(definition.getScope());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
