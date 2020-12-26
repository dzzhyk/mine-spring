package com.yankaizhang.spring.beans.factory.support;

import com.yankaizhang.spring.aop.annotation.Aspect;
import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;
import com.yankaizhang.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yankaizhang.spring.beans.factory.impl.RootBeanDefinition;
import com.yankaizhang.spring.beans.holder.BeanWrapper;
import com.yankaizhang.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.Set;

import static com.yankaizhang.spring.beans.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 具有全部功能的bean工厂抽象类
 * 继承自{@link AbstractConfigurableBeanFactory}抽象类，额外实现了全功能接口{@link CompletedBeanFactory}
 * 添加了默认的自动装配功能实现、默认的遍历功能实现
 * @author dzzhyk
 * @since 2020-12-21 18:33:16
 */
public abstract class AbstractCompletedBeanFactory extends AbstractConfigurableBeanFactory
        implements CompletedBeanFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractCompletedBeanFactory.class);

    @Override
    @SuppressWarnings("unchecked")
    public <T> T createBean(Class<T> beanClass) throws RuntimeException {
        // Use prototype bean definition, to avoid registering bean as dependent bean.
        RootBeanDefinition bd = new RootBeanDefinition(beanClass);
        bd.setScope(SCOPE_PROTOTYPE);
        return (T) createBean(beanClass.getName(), bd, null);
    }

    @Override
    public Object createBean(Class<?> beanClass, int autowireMode, boolean dependencyCheck) throws RuntimeException {
        // Use prototype bean definition, to avoid registering bean as dependent bean.
        RootBeanDefinition bd = new RootBeanDefinition(beanClass, autowireMode, dependencyCheck);
        bd.setScope(SCOPE_PROTOTYPE);
        return createBean(beanClass.getName(), bd, null);
    }

    /**
     * 创建bean对象方法
     * 主要的过程有实例化、应用处理器、属性注入
     * 返回{@link com.yankaizhang.spring.beans.holder.BeanWrapper}包装的对象
     * @param beanName bean名称
     * @param beanDef bean定义
     * @param args 额外参数
     * @return 创建完成的包装类对象
     * @throws RuntimeException 创建时异常
     */
    @Override
    protected Object createBean(String beanName, RootBeanDefinition beanDef, Object[] args) throws RuntimeException {
        // 1. 允许InstantiationAwareBeanPostProcessor返回一个不为Null的代理对象来替代这个bean
        try {
            Object bean = resolveBeforeInstantiation(beanName, beanDef);
            if (bean != null) {
                // 如果就是想把这个bean替换成要求的代理对象，直接返回就好了
                return bean;
            }
        }
        catch (Throwable ex) {
            throw new RuntimeException("实例化前InstantiationAwareBeanPostProcessor执行失败 => " + beanName, ex);
        }

        // 2. 需要代理的情况已经解决了，现在正式创建bean对象
        Object beanInstance = doCreateBean(beanName, beanDef, args);
        log.debug("成功完成bean对象的创建和初始化 : {}", beanName);
        return beanInstance;
    }

    /**
     * 在bean初始化之前解析该bean的前置处理器
     * 如果返回了一个非null代理对象，就跳过后面的bean创建
     * @param beanName bean名称
     * @param beanDef bean定义
     * @return 可能的代理对象
     */
    private Object resolveBeforeInstantiation(String beanName, RootBeanDefinition beanDef) {
        Object bean = null;
        if (beanDef.beforeInstantiationResolved == null || beanDef.beforeInstantiationResolved.equals(Boolean.TRUE)){
            if (hasInstantiationAwareBeanPostProcessors()){

                Class<?> targetType = beanDef.getTargetType();
                // 如果beanDef已经指定了需要的Type才创建
                if (targetType != null) {
                    bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                    if (bean != null) {
                        bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
                    }
                }
            }
        }
        return bean;
    }

    /**
     * 执行所有InstantiationAwareBeanPostProcessor
     * @param beanClass bean类对象
     * @param beanName bean名称
     * @return 可能的bean对象
     */
    private Object applyBeanPostProcessorsBeforeInstantiation(Class<?> beanClass, String beanName) {
        for (InstantiationAwareBeanPostProcessor bp : getInstantiationAware()) {
            Object result = bp.postProcessBeforeInstantiation(beanClass, beanName);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 真正创建一个bean对象
     * @param beanName bean名称
     * @param beanDef bean定义
     * @param args 额外参数
     * @return 创建好的bean对象的包装对象
     */
    private Object doCreateBean(String beanName, RootBeanDefinition beanDef, Object[] args) throws RuntimeException {
        // Instantiate the bean.
        BeanWrapper instanceWrapper = null;
        instanceWrapper = createBeanInstance(beanName, beanDef, args);
        Object bean = instanceWrapper.getWrappedInstance();
        Class<?> beanType = instanceWrapper.getWrappedClass();

        if (beanType != null) {
            beanDef.setTargetType(beanType);
        }

        // 执行处理器
        synchronized (beanDef.postProcessingLock) {
            if (!beanDef.postProcessed) {
                try {
                    applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
                }
                catch (Throwable ex) {
                    throw new BeanCreationException(mbd.getResourceDescription(), beanName,
                            "Post-processing of merged bean definition failed", ex);
                }
                mbd.postProcessed = true;
            }
        }

        // Eagerly cache singletons to be able to resolve circular references
        // even when triggered by lifecycle interfaces like BeanFactoryAware.
        boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
                isSingletonCurrentlyInCreation(beanName));
        if (earlySingletonExposure) {
            if (logger.isTraceEnabled()) {
                logger.trace("Eagerly caching bean '" + beanName +
                        "' to allow for resolving potential circular references");
            }
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
        }

        // Initialize the bean instance.
        Object exposedObject = bean;
        try {
            populateBean(beanName, mbd, instanceWrapper);
            exposedObject = initializeBean(beanName, exposedObject, mbd);
        }
        catch (Throwable ex) {
            if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
                throw (BeanCreationException) ex;
            }
            else {
                throw new BeanCreationException(
                        mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
            }
        }

        if (earlySingletonExposure) {
            Object earlySingletonReference = getSingleton(beanName, false);
            if (earlySingletonReference != null) {
                if (exposedObject == bean) {
                    exposedObject = earlySingletonReference;
                }
                else if (!this.allowRawInjectionDespiteWrapping && hasDependentBean(beanName)) {
                    String[] dependentBeans = getDependentBeans(beanName);
                    Set<String> actualDependentBeans = new LinkedHashSet<>(dependentBeans.length);
                    for (String dependentBean : dependentBeans) {
                        if (!removeSingletonIfCreatedForTypeCheckOnly(dependentBean)) {
                            actualDependentBeans.add(dependentBean);
                        }
                    }
                    if (!actualDependentBeans.isEmpty()) {
                        throw new BeanCurrentlyInCreationException(beanName,
                                "Bean with name '" + beanName + "' has been injected into other beans [" +
                                        StringUtils.collectionToCommaDelimitedString(actualDependentBeans) +
                                        "] in its raw version as part of a circular reference, but has eventually been " +
                                        "wrapped. This means that said other beans do not use the final version of the " +
                                        "bean. This is often the result of over-eager type matching - consider using " +
                                        "'getBeanNamesForType' with the 'allowEagerInit' flag turned off, for example.");
                    }
                }
            }
        }

        // Register bean as disposable.
        try {
            registerDisposableBeanIfNecessary(beanName, bean, mbd);
        }
        catch (BeanDefinitionValidationException ex) {
            throw new BeanCreationException(
                    mbd.getResourceDescription(), beanName, "Invalid destruction signature", ex);
        }

        return exposedObject;
    }

    /**
     * 创建bean实例
     * @param beanName bean名称
     * @param beanDef bean定义
     * @param args 额外参数
     * @return {@link BeanWrapper}包装类对象
     */
    protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition beanDef, Object[] args) {

        Class<?> beanClass = beanDef.getTargetType();

        if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !beanDef.isNonPublicAccessAllowed()) {
            throw new RuntimeException("beanClass不是public类对象, 但是bean定义不允许访问非public类 : " + beanClass.getName());
        }

        // TODO: 这里真正创建实例的方法非常简单，应该改为通过构造器和额外参数创建，这里就直接使用反射或者工厂实例创建了
        BeanWrapper beanWrapper = instantiateBean(beanDef);
        return beanWrapper;
    }


    /**
     * 实例化Bean定义
     * 实例化的结果是单例IoC中的Bean对象
     * com.yankaizhang.test.service.impl.TestServiceImpl -> TestServiceImpl@1024
     */
    private BeanWrapper instantiateBean(RootBeanDefinition beanDefinition) throws RuntimeException {

        BeanWrapper instance = null;

        String factoryMethodName = beanDefinition.getFactoryMethodName();
        String beanClassName = beanDefinition.getBeanClassName();

        // 根据factoryMethodName是否为null来区分反射实例和工厂实例
        if (null == factoryMethodName){

            // 如果这个类拥有beanClassName信息，首先检查是否有beanClassName
            // 使用beanClassName实例化对象 标准反射

            try {
                if (containsBean(beanClassName)){
                    // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                    instance = (BeanWrapper) getBean(beanClassName);
                } else {

                    // TODO: 这里可以改为使用策略模式
                    Class<?> clazz = Class.forName(beanClassName);
                    Object ins = clazz.newInstance();
                    instance = new BeanWrapper(ins);

                    // 注解AOP支持
                    if (clazz.isAnnotationPresent(Aspect.class)){
                        // 如果是切面类，加入切面容器
                        aspectBeanInstanceCache.add(clazz);
                    }

                }
            }catch (InstantiationException | ClassNotFoundException | IllegalAccessException e){
                throw new RuntimeException("bean实例化错误：" + beanClassName, e);
            }
        }else{

            // 获取工厂类对象beanName
            String factoryBeanName = beanDefinition.getFactoryBeanName();
            if (null != factoryBeanName && !"".equals(factoryBeanName.trim())){
                // 这个beanDefinition拥有factoryBeanName信息
                // 说明应该是使用工厂方法实例化的（@Bean方式）
                // 因此首先获取工厂bean

                if (StringUtils.isEmpty(factoryBeanName) || StringUtils.isEmpty(factoryMethodName)) {
                    throw new RuntimeException("bean实例化错误：未知factoryBeanName或factoryMethodName");
                }

                // 使用工厂对象创建Bean对象并且加入
                BeanWrapper wrappedFactoryBean = (BeanWrapper) getBean(factoryBeanName);

                if (null == wrappedFactoryBean){
                    throw new RuntimeException("bean实例化错误：获取对象工厂bean失败");
                }

                Object factoryBeanInUse = wrappedFactoryBean.getWrappedInstance();
                Class<?> factoryClazz = factoryBeanInUse.getClass();
                Method factoryMethod = null;
                try {
                    factoryMethod = factoryClazz.getMethod(factoryMethodName);
                    Object ins = factoryMethod.invoke(factoryBeanInUse);
                    instance = new BeanWrapper(ins);

                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException("使用工厂创建bean实例失败 => "+beanClassName, e);
                }

            }else{
                throw new RuntimeException("bean实例化错误：未知的实例化方式 => " + beanDefinition.getBeanClassName());
            }
        }

        return instance;
    }
}
