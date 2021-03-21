package com.yankaizhang.spring.beans.factory.support;


import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.accessor.BeanMetadataAttributeAccessor;
import com.yankaizhang.spring.beans.factory.AutowiredBeanFactory;
import com.yankaizhang.spring.beans.holder.MutablePropertyValues;
import com.yankaizhang.spring.beans.holder.ConstructorArgumentValues;


/**
 * AbstractBeanDefinition抽象实现<br/>
 * 把bean的元数据和bean的定义接口联系起来了，数据和操作结合了<br/>
 * BeanDefinition包含了数据<br/>
 * BeanMetadataAttributeAccessor包含了操作<br/>
 * 另外在这个抽象类里面还定义了所有BeanDefinition都应该具有的属性，结合实现BeanDefinition里面的方法就可以把这些属性暴露出去
 * @author dzzhyk
 * @since 2021-03-08 17:45:40
 */
public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor
    implements BeanDefinition, Cloneable {

    /**
     * 默认是单例
     */
    public static final String SCOPE_DEFAULT = SCOPE_SINGLETON;

    /**
     * 自动装配模式
     */
    public static final int AUTOWIRE_NO = AutowiredBeanFactory.AUTOWIRE_NO;
    public static final int AUTOWIRE_BY_NAME = AutowiredBeanFactory.AUTOWIRE_BY_NAME;
    public static final int AUTOWIRE_BY_TYPE = AutowiredBeanFactory.AUTOWIRE_BY_TYPE;
    public static final int AUTOWIRE_CONSTRUCTOR = AutowiredBeanFactory.AUTOWIRE_CONSTRUCTOR;

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
    private volatile String beanClassName;
    private volatile Class<?> beanClass;

    private String scope = SCOPE_DEFAULT;
    private boolean abstractFlag = false;

    /** 默认不是懒加载 */
    private Boolean lazyInit = false;

    /** 默认不进行自动装配 */
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
    private boolean nonPublicAccessAllowed = true;

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
        if (this.beanClassName == null){
            throw new Exception("bean定义验证失败，缺少beanClassName");
        }else if(this.beanClass == null){
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
        this.beanClassName = beanClassName;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getBeanClassName() {
        return this.beanClassName;
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
        return SCOPE_SINGLETON.equals(this.scope);
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

    @Override
    public Class<?> getBeanClass() {
//        if (beanClass == null){
//            throw new RuntimeException("bean定义的beanClass 为 null");
//        }
//        if (!(beanClass instanceof Class)){
//            throw new Exception("该bean定义的beanClass尚未从beanName解析到实际Class类 => " + this.getBeanClassName());
//        }
        return beanClass;
    }

    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
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
        if (this.propertyValues == null){
            this.propertyValues = new MutablePropertyValues();
        }
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

    public boolean isNonPublicAccessAllowed() {
        return nonPublicAccessAllowed;
    }

    public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
        this.nonPublicAccessAllowed = nonPublicAccessAllowed;
    }
}
