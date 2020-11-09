package com.yankaizhang.springframework.beans.factory.support;

import com.yankaizhang.springframework.beans.MutablePropertyValues;

import java.util.Objects;

/**
 * Bean定义
 * 相当于保存在内存中的配置
 * beanClassName：接口的ClassName是他的实现类
 * factoryBeanName：@Autowired接口就等于接口名字
 * id是接口名字，但是对应的实例是相应实现类的bean
 */
public class RootBeanDefinition {

    private String beanClassName = null;   // 用于实例化该bean定义的全类名
    private boolean lazyInit = false;   // 默认关闭懒加载
    private String factoryBeanName; // IoC容器中该Bean对象的name
    private String factoryBeanClassName;    // 工厂beanClassName全类名
    private String factoryMethodName = null;   // 创建该BeanDefinition的工厂方法name

    MutablePropertyValues propertyValues = null;   // 该bean定义传入的配置好的属性值

    /**
     * @param beanClassName com.yankaizhang.test.service.impl.TestServiceImpl
     * @param factoryBeanName testService
     */
    public RootBeanDefinition(String beanClassName, String factoryBeanName, boolean lazyInit) {
        this.beanClassName = beanClassName;
        this.factoryBeanName = factoryBeanName;
        this.lazyInit = lazyInit;
    }

    /**
     * 工厂方法创建类型
     * @param factoryBeanName 创建bean实例用的工厂beanName
     * @param factoryMethodName 创建bean实例用的工厂方法name
     */
    public RootBeanDefinition(String factoryBeanName, String factoryMethodName) {
        this.factoryBeanName = factoryBeanName;
        this.factoryMethodName = factoryMethodName;
    }

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    public String getFactoryBeanClassName() {
        return factoryBeanClassName;
    }

    public void setFactoryBeanClassName(String factoryBeanClassName) {
        this.factoryBeanClassName = factoryBeanClassName;
    }

    @Override
    public String toString() {
        return "BeanDefinition{" +
                "beanClassName='" + beanClassName + '\'' +
                ", lazyInit=" + lazyInit +
                ", factoryBeanName='" + factoryBeanName + '\'' +
                ", factoryBeanClassName='" + factoryBeanClassName + '\'' +
                ", factoryMethodName='" + factoryMethodName + '\'' +
                ", propertyValues=" + propertyValues +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RootBeanDefinition that = (RootBeanDefinition) o;
        return lazyInit == that.lazyInit &&
                Objects.equals(beanClassName, that.beanClassName) &&
                Objects.equals(factoryBeanName, that.factoryBeanName) &&
                Objects.equals(factoryBeanClassName, that.factoryBeanClassName) &&
                Objects.equals(factoryMethodName, that.factoryMethodName) &&
                Objects.equals(propertyValues, that.propertyValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beanClassName, lazyInit, factoryBeanName, factoryBeanClassName, factoryMethodName, propertyValues);
    }
}
