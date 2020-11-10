package com.yankaizhang.springframework.beans;

import com.yankaizhang.springframework.beans.factory.config.ConstructorArgumentValues;
import com.yankaizhang.springframework.core.AttributeAccessor;


/**
 * Bean定义的接口
 *
 *
 * 我们的bean定义得有来源BeanMetadataElement，得可以操作属性AttributeAccessor。
 * 比如我给你一个类，你要将他转换成bean，你到知道他的一些信息吧，不然等于什么都没有，别说获取注解什么的了，
 * 当然同时可能这些信息不够，所以这只是一个接口，还要扩展。
 *
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * BeanDefinition描述了一个bean实例，包含了该实例的属性信息，构造函数信息，以及更多信息。
 *
 * This is just a minimal interface: The main intention is to allow a
 * BeanFactoryPostProcessor to introspect and modify property values
 * and other bean metadata.
 *
 * 这个接口存在的目的主要是允许BeanFactoryPostProcessor这个BeanDefinition后置处理器介入处理这个对象，
 * 从而达到了提供对BeanDefinition自定义功能（modify）
 */

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

    /**
     * Scope identifier for the standard singleton scope
     * 单例作用域的标识符
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * Scope identifier for the standard prototype scope
     * 多例作用域的标识符
     */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 这三个常量是这个BeanDefinition所定义的bean对象的角色
     * 普通应用bean，一般是用户的bean对象
     * 用于支持的bean对象，一般是某些更加复杂的beanDefinition的支撑部分
     * Spring内置bean，一般是内部的bean对象
     */
    int ROLE_APPLICATION = 0;
    int ROLE_SUPPORT = 1;
    int ROLE_INFRASTRUCTURE = 2;


     //  ————————————————————可以修改的属性—————————————————————
     //  其实就是一些get,set方法，这些方法在后置处理器中用来自定义当前的BeanDefinition

    /**
     * 父类名称
     */
    void setParentName(String parentName) throws Exception;
    String getParentName();

    /**
     * bean实例的类名
     */
    void setBeanClassName(String beanClassName);
    String getBeanClassName();


    /**
     * bean的生命周期
     */
    void setScope(String scope);
    String getScope();

    /**
     * 懒加载
     */
    void setLazyInit(boolean lazyInit);
    boolean isLazyInit();

    /**
     * 需要先加载的依赖的类名字数组
     */
    void setDependsOn(String... dependsOn);
    String[] getDependsOn();

    /**
     * 是否可以 作为其他类的自动装配对象
     */
    void setAutowireCandidate(boolean autowireCandidate);
    boolean isAutowireCandidate();

    /**
     * 设置是否优先装配
     */
    void setPrimary(boolean primary);
    boolean isPrimary();

    /**
     * bean创建工厂FactoryBean的名字
     */
    void setFactoryBeanName(String factoryBeanName);
    String getFactoryBeanName();


    /**
     * bean创建工厂的工厂方法名称
     */
    void setFactoryMethodName(String factoryMethodName);
    String getFactoryMethodName();

    /**
     * 构造函数的参数
     */
    ConstructorArgumentValues getConstructorArgumentValues();
    default boolean hasConstructorArgumentValues() {
        return !getConstructorArgumentValues().isEmpty();
    }

    /**
     * bean的已经定义的属性（例如property标签定义的属性）
     */
    MutablePropertyValues getPropertyValues();
    default boolean hasPropertyValues() {return !getPropertyValues().isEmpty();}

    /**
     * bean的初始化方法
     */
    void setInitMethodName(String initMethodName);
    String getInitMethodName();


    /**
     * bean的销毁后调用方法
     */
    void setDestroyMethodName(String destroyMethodName);
    String getDestroyMethodName();

    /**
     * 设置bean的角色
     */
    void setRole(int role);
    int getRole();

    /**
     * bean的描述语句
     */
    void setDescription(String description);
    String getDescription();


    // ————————————————————只读属性—————————————————————

    boolean isSingleton();
    boolean isPrototype();
    boolean isAbstract();
    /**
     * 获取原始的BeanDefinition（未被修改的）
     */
    BeanDefinition getOriginatingBeanDefinition();
}
