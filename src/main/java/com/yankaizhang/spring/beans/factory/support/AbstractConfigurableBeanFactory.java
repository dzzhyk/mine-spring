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
import com.yankaizhang.spring.util.Assert;
import com.yankaizhang.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
     * 这个容器一般存放扫描到的Bean单例类对象
     */
    private Map<String, Object> singletonIoc = new ConcurrentHashMap<>(256);

    /**
     * 单例工厂容器
     * 这个容器存放可能的bean单例的工厂对象，通过单例工厂可以创建得到一个单例对象
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new ConcurrentHashMap<>(16);


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
            Object singletonObject = getSingleton(beanName);
            if (singletonObject != null){
                bean = getObjectForBeanInstance(singletonObject, beanName);
            }
            else {

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

                bean = getSingleton(tempBeanName);

                if (bean != null){
                    return (T) bean;
                }

                String[] dependsOn = beanDefinition.getDependsOn();

                // 检查依赖情况
                if (dependsOn != null) {
                    for (String dep : dependsOn) {
                        // 检查是否有循环依赖，否则会死循环
                        BeanDefinition depBeanDef = getBeanDefinition(dep);
                        String[] defDependsOn = depBeanDef.getDependsOn();
                        for (String depdep : defDependsOn) {
                            if (depdep.equals(beanName)){
                                throw new RuntimeException("存在循环依赖 => [" + beanName + " <=> " + dep + " ]");
                            }
                        }
                        try {
                            getBean(dep);
                        } catch (RuntimeException ex) {
                            throw new RuntimeException("获取" + beanName + "的依赖bean失败 => " + dep);
                        }
                    }
                }

                if (beanDefinition.isSingleton()){
                    // 这里调用子类实现的createBean方法创建完整的单例对象，singletonIoc中保存的都是非包装类的原始对象
                    Object wrappedBean = getSingleton(beanName, () -> createBean(beanName, beanDefinition, args));
                    // 最后要获取一下
                    bean = getObjectForBeanInstance(wrappedBean, beanName);
                }else if (beanDefinition.isPrototype()){
                    Object wrappedBean = createBean(beanName, beanDefinition, args);
                    bean = getObjectForBeanInstance(wrappedBean, beanName);
                }else{
                    throw new RuntimeException("目前只支持创建单例、多例对象 => " + beanName);
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
     * 在容器中获取可能的已经初始化的对象
     * @param beanName bean名称
     * @return 可能的已创建的对象
     */
    private Object getSingleton(String beanName){
         Object singletonObject = this.singletonIoc.get(beanName);
         if (singletonObject == null){
             synchronized (this.singletonIoc) {
                 singletonObject = this.singletonIoc.get(beanName);
                 if (singletonObject == null) {
                     // 获取可能的单例工厂对象
                     ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
                     if (singletonFactory != null) {
                         singletonObject = singletonFactory.getObject();
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
    private Object getSingleton(String beanName, ObjectFactory<?> singletonFactory){
        Assert.notNull(beanName, "beanName不能为null");
        synchronized (this.singletonIoc) {
            Object singletonObject = this.singletonIoc.get(beanName);
            if (singletonObject == null) {
                log.debug("创建单例对象 : {}", beanName);
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
                }
                // 如果创建成功，加入容器
                if (newSingleton) {
                    synchronized (this.singletonIoc) {
                        this.singletonIoc.put(beanName, singletonObject);
                    }
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
        Object singletonObject = this.getSingleton(beanName);
        if (singletonObject == null){
            return parentBeanFactory != null && getParentBeanFactory().isSingleton(beanName);
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
    public void destroyBean(String beanName, Object wrappedBean) {
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

            Class<?> beanInstanceClass = ((BeanWrapper) wrappedBean).getWrappedClass();
            Method destroyMethod = beanInstanceClass.getMethod(destroyMethodName);
            destroyMethod.invoke(((BeanWrapper) wrappedBean).getWrappedInstance());
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
}
