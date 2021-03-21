package com.yankaizhang.spring.beans.factory.support;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.FactoryBean;
import com.yankaizhang.spring.beans.factory.ObjectFactory;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.beans.factory.ConfigurableBeanFactory;
import com.yankaizhang.spring.beans.factory.config.DestructionAwareBeanPostProcessor;
import com.yankaizhang.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yankaizhang.spring.beans.factory.generic.GenericBeanDefinition;
import com.yankaizhang.spring.beans.holder.BeanWrapper;
import com.yankaizhang.spring.core.LinkedMultiValueMap;
import com.yankaizhang.spring.util.Assert;
import com.yankaizhang.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 顶层的抽象bean工厂实现，提供了默认的bean工厂方法，包括配置方法
 * @author dzzhyk
 * @since 2020-12-21 18:31:52
 */
public abstract class AbstractConfigurableBeanFactory implements ConfigurableBeanFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractConfigurableBeanFactory.class);

    /** 父类工厂对象 */
    private BeanFactory parentBeanFactory;

    /**
     * 单例IoC容器
     * 这个容器一般存放扫描到的Bean单例对象
     */
    private Map<String, Object> singletonIoc = new ConcurrentHashMap<>(256);

    /**
     * 单例工厂容器
     * 这个容器存放可能的bean单例的工厂对象，通过单例工厂可以创建得到一个单例对象
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>(16);

    /** 半实例化的bean对象，用于解决循环依赖 */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /** 正在创建的单例对象名称集合 */
    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    /** 正在创建的原型对象名称集合 */
    private final ThreadLocal<Object> prototypesCurrentlyInCreation = new ThreadLocal<>();

    /** bean处理器，目前所有的处理器全部放在一起了 */
    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();


    @Override
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }

    @Override
    public Object getBean(String beanName) throws RuntimeException  {
        return doGetBean(beanName, null);
    }

    @Override
    public <T> T getBean(Class<T> beanClass) throws RuntimeException  {
        return doGetBean(null, beanClass);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanClass) throws RuntimeException  {
        return doGetBean(beanName, beanClass);
    }

    /**
     * 真正的获取bean对象函数
     * 1. 首先检查是否已经创建了单例对象
     * 2. 如果存在父类工厂，向父类工厂发起检查doGetBean
     * 3. 如果没有父类工厂，获取该beanName对应的bean定义，创建一个新的bean实例
     * @param beanName  bean对象名称
     * @return bean对象 (可能为null)
     */
    @SuppressWarnings("all")
    protected <T> T doGetBean(String beanName, Class<T> beanClass, Object... args) throws RuntimeException {

        if (StringUtils.isEmpty(beanName) && beanClass == null){
            return null;
        }

        Object bean = null;

        if (!StringUtils.isEmpty(beanName)){

            // 检查是否已经有了实例化好的单例bean，或者使用单例工厂获取一个工厂
            // 这里要检查三级缓存中是否存在正在创建的bean对象
            Object singletonObject = getSingleton(beanName, true);
            if (singletonObject != null){
                bean = getObjectForBeanInstance(singletonObject, beanName);
            }
            else {

                // 如果是原型对象，并且是循环依赖的，直接抛出异常，因为mine-spring不处理原型循环依赖
                if (isPrototypeCurrentlyInCreation(beanName)){
                    throw new RuntimeException("原型对象可能存在循环引用，请检查代码 => " + beanName);
                }

                // 单例没找到，检查是否有父类bean工厂
                BeanFactory parentBeanFactory = getParentBeanFactory();
                // 如果有父类并且本容器中没有bean定义
                if (parentBeanFactory != null && !containsBeanDefinition(beanName)){
                    // 去父类找
                    if (parentBeanFactory instanceof AbstractConfigurableBeanFactory) {
                        return ((AbstractConfigurableBeanFactory) parentBeanFactory).doGetBean(beanName, beanClass);
                    } else if (beanClass != null) {
                        // 如果父类不是AbstractConfigurableBeanFactory抽象类的实现，需要特别判断beanClass!=null的情况
                        return parentBeanFactory.getBean(beanName, beanClass);
                    } else {
                        return (T) parentBeanFactory.getBean(beanName);
                    }
                }

                // 没有可创建单例，没有父类，只能自己创建一个bean实例对象了
                // 获取到的可能不是GenericBeanDefinition类型，在创建的时候需要统一使用GenericBeanDefinition，子类要强转一下
                BeanDefinition temp;
                try {
                    temp = getBeanDefinition(beanName);
                }catch (Exception e){
                    throw new RuntimeException("未找到beanName为 " + beanName + " 的bean对象");
                }

                GenericBeanDefinition beanDefinition = (GenericBeanDefinition) temp;

                // 如果已经有该bean定义指定的bean对象的单例对象了，就直接获取返回 - 这种情况一般适用于接口对应的实例对象为实现类对象
                String beanClassName = temp.getBeanClassName();
                String tempBeanName = StringUtils.toLowerCase(beanClassName.substring(beanClassName.lastIndexOf(".")+1));

                bean = getSingleton(tempBeanName, false);

                if (bean != null){
                    return (T) bean;
                }


                if (beanDefinition.isSingleton()){

                    // 这里调用子类实现的createBean方法创建完整的单例对象，singletonIoc中保存原始单例对象
                    Object wrappedBean = getSingleton(beanName, () -> createBean(beanName, beanDefinition, args));
                    // 最后要获取一下
                    bean = getObjectForBeanInstance(wrappedBean, beanName);

                }else if (beanDefinition.isPrototype()){
                    Object prototypeInstance = null;
                    try {
                        beforePrototypeCreation(beanName);
                        prototypeInstance = createBean(beanName, beanDefinition, args);
                    }
                    finally {
                        afterPrototypeCreation(beanName);
                    }
                    bean = getObjectForBeanInstance(prototypeInstance, beanName);

                }else{
                    throw new RuntimeException("目前只支持创建单例、原型对象 => " + beanName);
                }

            }

            if (bean != null && beanClass != null){
                if (beanClass.isAssignableFrom(bean.getClass())){
                    return (T) bean;
                }
                throw new RuntimeException("未找到beanName为 " + beanName + " , 类型为 " + beanClass.getName() + " 的bean对象");
            } else if(beanClass == null){
                return (T) bean;
            }
        }

        if (beanClass != null){
            List<Object> ans = new ArrayList<>(16);
            for (Map.Entry<String, Object> entry : this.singletonIoc.entrySet()) {
                BeanWrapper wrapper = (BeanWrapper) entry.getValue();
                if (beanClass.isAssignableFrom(wrapper.getWrappedClass())){
                    ans.add(wrapper.getWrappedInstance());
                }
            }
            if (ans.size() <= 0){
                throw new RuntimeException("未找到类型为 " + beanClass.getName() + " 的bean对象");
            }else if (ans.size() == 1){
                return (T) ans.get(0);
            }else{
                throw new RuntimeException("存在多个相同类型的bean对象，无法通过类型确定所需bean对象，推荐指明beanName => " + beanClass.getName());
            }
        }

        throw new RuntimeException("beanName 与 beanClass 均为null或空值，找不到bean对象");
    }

    /**
     * 现在拿到手里的已经是准备完成的、前后置处理完成的bean对象
     * 这个bean对象可能是一个工厂类对象，所以在Spring里面，如果尝试获取一个工厂类bean对象，会返回他的创建后对象
     * 这个方法就是用来判断工厂对象，如果是工厂对象，返回工厂的产品
     * @param bean 原始bean对象
     * @param beanName bean名称
     * @return 创建好的bean对象
     */
    @SuppressWarnings("all")
    private Object getObjectForBeanInstance(Object bean, String beanName) {
        Object wrappedInstance = bean;
        if (bean instanceof BeanWrapper){
            wrappedInstance = ((BeanWrapper) bean).getWrappedInstance();
            // 如果是工厂对象
            if (wrappedInstance instanceof FactoryBean){
                return getObjectFactoryProduct(((FactoryBean) wrappedInstance), beanName);
            }
        }else if (bean instanceof FactoryBean){
            return getObjectFactoryProduct(((FactoryBean) bean), beanName);
        }
        return wrappedInstance;
    }

    private Object getObjectFactoryProduct(FactoryBean<?> factoryBean, String beanName){
        try {
            return factoryBean.getObject();
        } catch (Exception e) {
            log.error("获取工厂对象的产品失败 => {}", beanName);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在容器中获取可能的已经初始化、半初始化的对象
     * @param beanName bean名称
     * @param allowEarlyReference 是否允许提前使用三级缓存创建bean对象，从而解决循环依赖问题，如果为false，就只从一级二级缓存中寻找
     * @return 可能的已创建的对象
     */
    protected Object getSingleton(String beanName, boolean allowEarlyReference){
        Object singletonObject = this.singletonIoc.get(beanName);
        if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
            // 如果当前bean对象正在初始化，说明发生了循环依赖
            singletonObject = this.earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference) {
                // 如果一级、二级缓存中都找不到，并且允许早期创建bean对象，就使用三级缓存创建
                synchronized (this.singletonIoc) {
                    singletonObject = this.singletonIoc.get(beanName);
                    if (singletonObject == null) {
                        singletonObject = this.earlySingletonObjects.get(beanName);
                        if (singletonObject == null) {
                            ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                            if (singletonFactory != null) {

                                // 如果二级缓存中没有，但是三级缓存中有，就使用三级缓存创建对象，并且放入二级缓存中
                                singletonObject = singletonFactory.getObject();
                                this.earlySingletonObjects.put(beanName, singletonObject);
                                // 执行aop，获取增强以后的对象，为了防止重复aop，将三级缓存删除，升级到二级缓存中
                                this.singletonFactories.remove(beanName);
                            }
                        }
                    }
                }
            }
        }
        return singletonObject;
    }

    /**
     * 使用指定的工厂对象创建单例对象
     * @param beanName bean名称
     * @param singletonFactory 指定的单例工厂对象
     * @return 创建好的单例对象
     */
    protected Object getSingleton(String beanName, ObjectFactory<?> singletonFactory){
        Assert.notNull(beanName, "beanName不能为null");
        synchronized (this.singletonIoc) {
            Object singletonObject = this.singletonIoc.get(beanName);
            if (singletonObject == null) {
                log.debug("创建单例对象 : {}", beanName);
                beforeSingletonCreation(beanName);
                boolean newSingleton = false;
                try {
                    singletonObject = singletonFactory.getObject();
                    newSingleton = true;
                }
                catch (RuntimeException ex) {
                    singletonObject = this.singletonIoc.get(beanName);
                    if (singletonObject == null) {
                        throw ex;
                    }
                }finally {
                    afterSingletonCreation(beanName);
                }
                // 如果创建成功，加入容器
                if (newSingleton) {
                    addSingleton(beanName, singletonObject);
                }
            }
            return singletonObject;
        }
    }


    @Override
    public boolean containsBean(String beanName) {
        return this.singletonIoc.get(beanName) != null ||
                (parentBeanFactory != null && this.getParentBeanFactory().containsBean(beanName));
    }

    @Override
    public boolean isSingleton(String beanName) throws Exception {
        Object singletonObject = this.getSingleton(beanName, false);
        if (singletonObject == null){
            return parentBeanFactory != null && getParentBeanFactory().isSingleton(beanName);
        }
        if (singletonObject instanceof FactoryBean){
            return ((FactoryBean<?>) singletonObject).isSingleton();
        }
        return true;
    }

    /*
        通用bean容器配置方法实现
     */

    @Override
    public void setParentBeanFactory(BeanFactory beanFactory) {
        this.parentBeanFactory = beanFactory;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    public boolean hasInstantiationAwareBeanPostProcessors(){
        for(BeanPostProcessor bp : beanPostProcessors){
            if (bp instanceof InstantiationAwareBeanPostProcessor){
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroyBean(String beanName, Object beanInstance) {
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        String destroyMethodName = beanDefinition.getDestroyMethodName();
        if(StringUtils.isEmpty(destroyMethodName)){
            return;
        }
        try {
            // 先执行销毁处理器
            // TODO: 注意，这里目前没有区分顺序order
            for(BeanPostProcessor processor : beanPostProcessors){
                if(processor instanceof DestructionAwareBeanPostProcessor){
                    ((DestructionAwareBeanPostProcessor) processor).postProcessBeforeDestruction(beanDefinition, beanName);
                }
            }

            Class<?> beanInstanceClass = beanInstance.getClass();
            Method destroyMethod = beanInstanceClass.getMethod(destroyMethodName);
            destroyMethod.invoke(beanInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroySingletons() {
        this.singletonIoc.clear();
        this.singletonFactories.clear();
        this.beanPostProcessors.clear();
    }

    /*
        下面是子类必须要实现的抽象方法
     */

    protected abstract boolean containsBeanDefinition(String beanName);

    protected abstract BeanDefinition getBeanDefinition(String beanDefName) throws RuntimeException;

    protected abstract Object createBean(String beanName, GenericBeanDefinition mbd, Object[] args) throws RuntimeException;

    @Override
    public Map<String, Object> getSingletonIoc() {
        return singletonIoc;
    }

    /**
     * 添加单例对象
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonIoc) {
            this.singletonIoc.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    /**
     * 添加单例工厂对象
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(singletonFactory, "单例工厂bean对象不能为null");
        synchronized (this.singletonIoc) {
            if (!this.singletonIoc.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
            }
        }
    }

    /**
     * 在单例对象创建之前，标记单例对象正在处于创建状态
     */
    protected void beforeSingletonCreation(String beanName) throws RuntimeException {
        if (!this.singletonsCurrentlyInCreation.add(beanName)) {
            throw new RuntimeException("单例Bean对象 " + beanName + " 当前正在被创建");
        }
    }

    /**
     * 在单例对象创建之后，将单例对象从正在创建状态删除
     */
    protected void afterSingletonCreation(String beanName) throws RuntimeException {
        if (!this.singletonsCurrentlyInCreation.remove(beanName)) {
            throw new RuntimeException("单例Bean对象 " + beanName + " 当前没有被创建");
        }
    }

    protected boolean isSingletonCurrentlyInCreation(String beanName){
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    /**
     * 在原型对象创建之前，标记该原型对象正在被创建<br/>
     * 因为每个线程从容器中获取原型对象的时候都需要分别记录当前线程的创建情况<br/>
     * 因此这里保存当前线程的原型创建情况的容器是{@link ThreadLocal}线程本地变量<br/>
     * 使用{@link ThreadLocal}，相当于在每个线程本地，都记录了各自的原型对象创建情况
     */
    protected void beforePrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal == null) {
            this.prototypesCurrentlyInCreation.set(beanName);
        }
        else if (curVal instanceof String) {
            // 如果当前线程创建了超过两种原型对象，就转换为set记录所有beanName
            Set<String> beanNameSet = new HashSet<>(2);
            beanNameSet.add((String) curVal);
            beanNameSet.add(beanName);
            this.prototypesCurrentlyInCreation.set(beanNameSet);
        }
        else {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.add(beanName);
        }
    }

    /**
     * 在创建原型对象之后，标记该原型对象创建完成，从记录中移除
     */
    protected void afterPrototypeCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        if (curVal instanceof String) {
            this.prototypesCurrentlyInCreation.remove();
        }
        else if (curVal instanceof Set) {
            Set<String> beanNameSet = (Set<String>) curVal;
            beanNameSet.remove(beanName);
            if (beanNameSet.isEmpty()) {
                // 如果全部移除了，一定要显式地调用ThreadLocal的remove方法清除线程本地变量，否则可能会内存泄漏
                this.prototypesCurrentlyInCreation.remove();
            }
        }
    }

    protected boolean isPrototypeCurrentlyInCreation(String beanName) {
        Object curVal = this.prototypesCurrentlyInCreation.get();
        return (curVal != null &&
                (curVal.equals(beanName) || (curVal instanceof Set && ((Set<?>) curVal).contains(beanName))));
    }
}
