package com.yankaizhang.springframework.beans.factory.support;

import com.yankaizhang.springframework.beans.MutablePropertyValues;

/**
 * Bean定义
 * 相当于保存在内存中的配置
 * beanClassName：接口的ClassName是他的实现类
 * factoryBeanName：@Autowired接口就等于接口名字
 * id是接口名字，但是对应的实例是相应实现类的bean
 */
public class BeanDefinition {
    private String beanClassName;   // 用于实例化该bean定义的全类名
    private boolean lazyInit = false;   // 默认关闭懒加载
    private String factoryBeanName; // IoC容器中该Bean对象的id

    MutablePropertyValues propertyValues;   // 该bean定义传入的配置好的属性值

    /**
     * @param beanClassName com.yankaizhang.test.service.impl.TestServiceImpl
     * @param factoryBeanName testService
     */
    public BeanDefinition(String beanClassName, String factoryBeanName) {
        this.beanClassName = beanClassName;
        this.factoryBeanName = factoryBeanName;
    }

    public BeanDefinition(String beanClassName, String factoryBeanName, boolean lazyInit) {
        this.beanClassName = beanClassName;
        this.factoryBeanName = factoryBeanName;
        this.lazyInit = lazyInit;
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
}
