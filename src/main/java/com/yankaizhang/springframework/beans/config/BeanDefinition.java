package com.yankaizhang.springframework.beans.config;

/**
 * Bean定义
 * 相当于保存在内存中的配置
 * beanClassName：接口的ClassName是他的实现类
 * factoryBeanName：@Autowired接口就等于接口名字
 * id是接口名字，但是对应的实例是相应实现类的bean
 */
public class BeanDefinition {
    private String beanClassName;   // 用于实例化该bean定义的真正ClassName
    private boolean lazyInit = false;
    private String factoryBeanName; // IoC容器中该Bean对象的id

    public BeanDefinition(String beanClassName, String factoryBeanName) {
        this.beanClassName = beanClassName;
        this.factoryBeanName = factoryBeanName;
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
}
