package com.yankaizhang.spring.beans.factory.support;


import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanMetadataAttributeAccessor;
import com.yankaizhang.spring.beans.MutablePropertyValues;
import com.yankaizhang.spring.beans.factory.config.ConstructorArgumentValues;


/**
 * AbstractBeanDefinition抽象实现
 * 把bean的元数据和bean的定义接口联系起来了，数据和操作结合了
 * BeanDefinition包含了数据
 * BeanMetadataAttributeAccessor包含了操作
 * 另外在这个抽象类里面还定义了所有BeanDefinition都应该具有的属性，结合实现BeanDefinition里面的方法就可以把这些属性暴露出去
 * @author dzzhyk
 * @since 2020-11-28 13:52:11
 */
public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor
    implements BeanDefinition, Cloneable {

    /**
     * 默认是单例
     */
    public static final String SCOPE_DEFAULT = "";

    /**
     * 自动装配模式
     */
    public static final int AUTOWIRE_NO = 1;
    public static final int AUTOWIRE_BY_NAME = 2;
    public static final int AUTOWIRE_BY_TYPE = 3;
    public static final int AUTOWIRE_CONSTRUCTOR = 4;

    /**
     * 使用none模式
     * none模式指的是不进行依赖检查
     */
    public static final int DEPENDENCY_CHECK_NONE = 0;
    /**
     * 使用object模式
     * object模式指的是对依赖的对象进行依赖检查
     */
    public static final int DEPENDENCY_CHECK_OBJECTS = 1;
    /**
     * 使用simple模式
     * simple模式是指对基本类型，字符串和集合进行依赖检查
     */
    public static final int DEPENDENCY_CHECK_SIMPLE = 2;
    /**
     * 使用all模式
     * all模式指的是对全部属性进行依赖检查
     */
    public static final int DEPENDENCY_CHECK_ALL = 3;


     // 简化后的BeanDefinition具有的一堆属性

    /**
     *  本类的beanClass，实例化的结果就是这个类
     *  这个beanClass可以是Class，也可以是String类型（全类名）
     */
    private volatile Object beanClass;

    private String scope = SCOPE_DEFAULT;
    private boolean abstractFlag = false;
    /**
     * 默认不是懒加载
     */
    private Boolean lazyInit = false;
    private int autowireMode = AUTOWIRE_NO;
    private int dependencyCheck = DEPENDENCY_CHECK_NONE;
    private String[] dependsOn;
    private boolean autowireCandidate = true;
    private boolean primary = false;
    private String factoryBeanName;
    private String factoryMethodName;
    private ConstructorArgumentValues constructorArgumentValues;
    private MutablePropertyValues propertyValues;
    private String initMethodName;
    private String destroyMethodName;
    private int role = BeanDefinition.ROLE_APPLICATION;
    private String description;

    public AbstractBeanDefinition() {
        this(null, null);
    }

    public AbstractBeanDefinition(BeanDefinition beanDefinition) {
        this(beanDefinition.getConstructorArgumentValues(), beanDefinition.getPropertyValues());
    }

    /**
     * 创建一个抽象BeanDefinition至少要提供两个信息
     * 当然这些信息也可以为空
     * @param constructorArgumentValues 构造函数的参数值
     * @param propertyValues bean实例化的时候的已知属性值
     */
    public AbstractBeanDefinition(ConstructorArgumentValues constructorArgumentValues, MutablePropertyValues propertyValues) {
        this.constructorArgumentValues = constructorArgumentValues;
        this.propertyValues = propertyValues;
    }


    /**
     * 验证BeanDefinition
     * 继承的子类可以重写这个方法
     */
    public void validate() throws Exception {
        if (this.beanClass == null){
            throw new Exception("bean定义验证失败，缺少beanClass");
        }
    }

    // ———————————————————下面的基本就是get、set方法了—————————————————————
    // 其实这个AbstractBeanDefinition就是个包含了非常多信息的包装类而已，提供了访问操作
    // 无论是新加入的属性访问，还是BeanDefinition实现来的属性访问方法最好都要实现


    @Override
    public boolean hasConstructorArgumentValues() {
        return (this.constructorArgumentValues != null && !this.constructorArgumentValues.isEmpty());
    }

    @Override
    public boolean hasPropertyValues() {
        return (this.propertyValues != null && !this.propertyValues.isEmpty());
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        this.beanClass = beanClassName;
    }

    public void setBeanClassName(Class<?> beanClassName) {
        this.beanClass = beanClassName;
    }

    /**
     * 这个方法要注意一下，因为在AbstractBeanDefinition里面beanClass可能是Object
     */
    @Override
    public String getBeanClassName() {
        Object beanClassObject = this.beanClass;
        if (beanClassObject instanceof Class){
            return ((Class<?>) beanClassObject).getName();
        }else{
            return (String) beanClassObject;
        }
    }

    @Override
    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    @Override
    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(this.scope) || SCOPE_DEFAULT.equals(this.scope);
    }

    @Override
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(this.scope);
    }

    @Override
    public boolean isAbstract() {
        return this.abstractFlag;
    }

    /**
     * 获取原始的BeanDefinition
     * TODO: 实现这个方法
     */
    @Override
    public BeanDefinition getOriginatingBeanDefinition() {
        return null;
    }

    public Object getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Object beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    public boolean isAbstractFlag() {
        return abstractFlag;
    }

    public void setAbstractFlag(boolean abstractFlag) {
        this.abstractFlag = abstractFlag;
    }

    public Boolean getLazyInit() {
        return lazyInit;
    }

    public int getAutowireMode() {
        return autowireMode;
    }

    public void setAutowireMode(int autowireMode) {
        this.autowireMode = autowireMode;
    }

    public int getDependencyCheck() {
        return dependencyCheck;
    }

    public void setDependencyCheck(int dependencyCheck) {
        this.dependencyCheck = dependencyCheck;
    }

    @Override
    public String[] getDependsOn() {
        return dependsOn;
    }

    @Override
    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public boolean isAutowireCandidate() {
        return autowireCandidate;
    }

    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public ConstructorArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    @Override
    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public String getInitMethodName() {
        return initMethodName;
    }

    @Override
    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

    @Override
    public String getDestroyMethodName() {
        return destroyMethodName;
    }

    @Override
    public void setDestroyMethodName(String destroyMethodName) {
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public int getRole() {
        return role;
    }

    @Override
    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }
}
