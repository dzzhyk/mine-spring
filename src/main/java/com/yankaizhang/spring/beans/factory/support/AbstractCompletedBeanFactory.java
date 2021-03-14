package com.yankaizhang.spring.beans.factory.support;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.PropertyValues;
import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;
import com.yankaizhang.spring.beans.factory.annotation.Autowired;
import com.yankaizhang.spring.beans.factory.annotation.Qualifier;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yankaizhang.spring.beans.factory.config.MergedBeanDefinitionPostProcessor;
import com.yankaizhang.spring.beans.factory.generic.GenericBeanDefinition;
import com.yankaizhang.spring.beans.holder.BeanWrapper;
import com.yankaizhang.spring.beans.holder.MutablePropertyValues;
import com.yankaizhang.spring.beans.holder.PropertyValue;
import com.yankaizhang.spring.context.annotation.Component;
import com.yankaizhang.spring.context.annotation.Controller;
import com.yankaizhang.spring.context.annotation.Service;
import com.yankaizhang.spring.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * 具有全部功能的bean工厂抽象类
 * 继承自{@link AbstractConfigurableBeanFactory}抽象类<br/>
 * 额外实现了全功能接口{@link CompletedBeanFactory}，添加了默认的自动装配功能实现、默认的遍历功能实现
 * @author dzzhyk
 * @since 2021-03-08 18:10:26
 */
public abstract class AbstractCompletedBeanFactory extends AbstractConfigurableBeanFactory
        implements CompletedBeanFactory {

    private static final Logger log = LoggerFactory.getLogger(AbstractCompletedBeanFactory.class);

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
    protected Object createBean(String beanName, GenericBeanDefinition beanDef, Object[] args) throws RuntimeException {
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
        Object beanInstance = null;
        try {
            beanInstance = doCreateBean(beanName, beanDef, args);
            log.debug("成功完成bean对象的创建和初始化 : {}", beanName);
        } catch (Exception e) {
            throw new RuntimeException("bean对象的创建和初始化过程出错 => " + beanName, e);
        }

        return beanInstance;
    }

    /**
     * 在bean初始化之前解析该bean的前置处理器
     * 如果返回了一个非null代理对象，就跳过后面的bean创建
     * @param beanName bean名称
     * @param beanDef bean定义
     * @return 可能的代理对象
     */
    private Object resolveBeforeInstantiation(String beanName, GenericBeanDefinition beanDef) throws Exception {
        Object bean = null;
        if (hasInstantiationAwareBeanPostProcessors()){
            Class<?> targetType = beanDef.getBeanClass();
            // 如果beanDef已经指定了需要的Type才创建
            if (targetType != null) {
                bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
                if (bean != null) {
                    // 如果实例化处理器返回了自定义代理对象，就直接进入对象创建结束环节
                    bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
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
        Object result = null;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if(bp instanceof InstantiationAwareBeanPostProcessor){
                result = ((InstantiationAwareBeanPostProcessor) bp).postProcessBeforeInstantiation(beanClass, beanName);
            }
        }
        log.debug("执行 :" + beanName + "的实例化bean对象前置处理器");
        return result;
    }

    /**
     * 真正创建一个bean对象
     * @param beanName bean名称
     * @param beanDef bean定义
     * @param args 额外参数
     * @return 创建好的bean对象的包装对象
     */
    private Object doCreateBean(String beanName, GenericBeanDefinition beanDef, Object[] args) throws Exception {

        // 如果当前bean定义没有Class对象
        if (beanDef.getBeanClass() == null){
            String beanClassName = beanDef.getBeanClassName();
            Class<?> clazz = Class.forName(beanClassName);
            beanDef.setBeanClass(clazz);
        }

        // 实例化bean对象
        BeanWrapper instanceWrapper = null;
        instanceWrapper = createBeanInstance(beanName, beanDef, args);
        Object bean = instanceWrapper.getWrappedInstance();
        Class<?> beanType = instanceWrapper.getWrappedClass();

        // 执行MergedBean处理器
        synchronized (beanDef.postProcessingLock) {
            if (!beanDef.postProcessed) {
                try {
                    applyMergedBeanDefinitionPostProcessors(beanDef, beanType, beanName);
                }
                catch (Throwable ex) {
                    throw new RuntimeException("预处理 merged bean definition 失败 => " + beanName);
                }
                beanDef.postProcessed = true;
            }
        }

        // 注入bean对象的属性
        log.debug("开始注入bean对象的属性 : {}", beanName);
        populateBean(beanName, beanDef, instanceWrapper);

        // 初始化对象
        log.debug("开始初始化bean对象 : {}", beanName);
        bean = initializeBean(bean, beanName, beanDef);

        return instanceWrapper;
    }

    /**
     * 执行MergedBeanDefinitionPostProcessor前置处理器
     * @param beanDef bean定义
     * @param beanType bean类型
     * @param beanName bean名称
     */
    private void applyMergedBeanDefinitionPostProcessors(GenericBeanDefinition beanDef, Class<?> beanType, String beanName) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof MergedBeanDefinitionPostProcessor){
                ((MergedBeanDefinitionPostProcessor) bp).postProcessMergedBeanDefinition(beanDef, beanType, beanName);
                log.debug("执行 :" + beanName + "的合并bean定义前置处理器");
            }
        }
    }

    /**
     * 创建bean实例
     * @param beanName bean名称
     * @param beanDef bean定义
     * @param args 额外参数
     * @return {@link BeanWrapper}包装类对象
     */
    protected BeanWrapper createBeanInstance(String beanName, GenericBeanDefinition beanDef, Object[] args) throws Exception {

        Class<?> beanClass = beanDef.getBeanClass();

        if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !beanDef.isNonPublicAccessAllowed()) {
            throw new RuntimeException("beanClass不是public类对象, 但是bean定义不允许访问非public类 : " + beanClass.getName());
        }

        // TODO: 这里真正创建实例的方法非常简单，其实应该通过构造器和额外参数创建，这里就直接使用反射或者工厂实例创建了
        return instantiateBean(beanDef);
    }

    /**
     * 实例化Bean定义
     * 实例化的结果是单例IoC中的Bean对象
     * com.yankaizhang.test.service.impl.TestServiceImpl -> TestServiceImpl@1024
     */
    private BeanWrapper instantiateBean(GenericBeanDefinition beanDefinition) throws RuntimeException {

        BeanWrapper instance = null;

        String factoryMethodName = beanDefinition.getFactoryMethodName();
        String beanClassName = beanDefinition.getBeanClassName();
        String beanName = StringUtils.toLowerCase(beanClassName.substring(beanClassName.lastIndexOf(".")+1));

        // 根据factoryMethodName是否为null来区分反射实例和工厂实例
        if (null == factoryMethodName){

            // 如果这个类拥有beanClassName信息，首先检查是否有beanClassName
            // 使用beanClassName实例化对象 标准反射

            try {
                if (containsBean(beanName)){
                    // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                    instance = (BeanWrapper) getBean(beanName);
                } else {

                    // TODO: 这里可以改为使用策略模式
                    Class<?> clazz = beanDefinition.getBeanClass();
                    Object ins = clazz.newInstance();
                    instance = new BeanWrapper(ins);

                }
            }catch (InstantiationException | IllegalAccessException e){
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
                Object factoryBeanInUse = getBean(factoryBeanName);

                if (null == factoryBeanInUse){
                    throw new RuntimeException("bean实例化错误：获取对象工厂bean失败");
                }

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


    /**
     * 对bean实例属性进行属性注入
     * TODO: 目前只支持手动使用@Autowired注入属性，所有bean对象默认不进行属性注入
     */
    private void populateBean(String beanName, GenericBeanDefinition beanDef, BeanWrapper beanWrapper) throws Exception{

        // 获取待属性注入的对象的真正实例
        Object instance = beanWrapper.getWrappedInstance();

        if (instance == null){
            if (beanDef.hasPropertyValues()){
                throw new RuntimeException("不能为null或空实例执行属性注入 => " + beanName);
            }else {
                // 如果是空对象，但是没有属性需要注入，就直接返回
                return;
            }
        }

        // 执行bean实例化处理器的after方法
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor){
                if (!((InstantiationAwareBeanPostProcessor) bp).postProcessAfterInstantiation(instance, beanName)){
                    return;
                }
            }
        }

        if (beanDef.getAutowireMode() == AUTOWIRE_BY_NAME || beanDef.getAutowireMode() == AUTOWIRE_BY_TYPE){
            log.warn("只支持显式地使用@Autowired注入属性，其他bean属性对象默认不进行属性注入");
        }

        // 为@Autowired注解标记的属性进行注入
        applyAutowiredBeanProperties(instance);

        // 注入用户自定义的属性进行
        applyBeanPropertyValues(instance, beanName);
    }

    @Override
    public void applyAutowiredBeanProperties(Object bean) throws Exception {

        // 目前只能给组件类中显式标注了@Autowired注解的属性执行自动属性注入
        Class<?> clazz = bean.getClass();
        if (!(clazz.isAnnotationPresent(Controller.class) ||
                clazz.isAnnotationPresent(Service.class) ||
                clazz.isAnnotationPresent(Component.class))) {
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {

            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            // @Autowired注解默认按照类型注入
            Class<?> type = field.getType();
            Map<String, ?> beansOfType = getBeansOfType(type);
            if(beansOfType.size() <= 0){
                log.warn("IoC容器未找到相应的bean对象 ==> " + type);
                continue;
            }

            Object toAutowiredInstance = null;
            if (beansOfType.size() == 1){
                for (Map.Entry<String, ?> entry : beansOfType.entrySet()) {
                    toAutowiredInstance = entry.getValue();
                }
            }else{
                // 可能会有多个同类型对象
                Qualifier qualifier = field.getAnnotation(Qualifier.class);
                if (qualifier == null) {
                    throw new Exception("存在多个相同类型的bean对象，无法执行属性注入类型，尝试使用@Qualifier注解指定bean名称 => " + type);
                }

                String autowiredBeanName = qualifier.value();
                // 获取装配目标对象
                toAutowiredInstance = getBean(autowiredBeanName);
            }

            field.setAccessible(true);

            if (null == toAutowiredInstance){
                log.warn("IoC容器未找到相应的bean对象 ==> " + type);
                continue;
            }

            // 将获取的包装类对象装配上去
            field.set(bean, toAutowiredInstance);
        }
    }

    @Override
    public void applyBeanPropertyValues(Object bean, String beanName) throws Exception {

        BeanDefinition beanDef = getBeanDefinition(beanName);
        PropertyValues pvs = beanDef.getPropertyValues();

        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor){
                pvs = ((InstantiationAwareBeanPostProcessor) bp).postProcessProperties(pvs, bean, beanName);
            }
        }

        if (pvs.isEmpty()){
            return;
        }

        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        // 这里可能会覆盖上面使用@Autowired注入的对象
        for (Field field : fields) {
            String fieldName = field.getName();
            if (pvs.contains(fieldName)){
                field.set(bean, pvs.getPropertyValue(fieldName).getValue());
            }
        }
    }

    @Override
    public Object initializeBean(Object existingBean, String beanName, BeanDefinition beanDef) throws RuntimeException {

        // 执行初始化前处理器
        Object wrappedBean = existingBean;
        applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);

        try {
            invokeInitMethods(wrappedBean, beanDef);
        }catch (Exception e){
            throw new RuntimeException("执行bean对象的初始化方法异常 => " + beanName);
        }

        // 执行初始化后处理器
        applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        return wrappedBean;
    }



    /**
     * 执行bean的定义好的初始化函数
     * @param bean bean对象
     * @param beanDefinition bean对象的bean定义
     */
    private void invokeInitMethods(Object bean, BeanDefinition beanDefinition) {
        String initMethodName = beanDefinition.getInitMethodName();
        if (!StringUtils.isEmpty(initMethodName)){
            try {
                Method initMethod = bean.getClass().getMethod(initMethodName);
                initMethod.invoke(bean, (Object[]) null);
                log.debug("执行 : "+ bean.getClass() +"对象的初始化方法 : " + initMethod.getName());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                log.warn("获取 : "+ bean.getClass() +"对象的初始化方法失败");
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                log.warn("执行 : " + bean.getClass() + "对象的初始化方法失败");
            }
        }
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws RuntimeException {
        Object wrappedBean = existingBean;
        try {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                wrappedBean = bp.postProcessBeforeInitialization(wrappedBean, beanName);
                log.debug("执行初始化前置处理器 : " + bp.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrappedBean;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws RuntimeException {
        Object wrappedBean = existingBean;
        try {
            for (BeanPostProcessor bp : getBeanPostProcessors()) {
                wrappedBean = bp.postProcessAfterInitialization(wrappedBean, beanName);
                log.debug("执行初始化后置处理器 : " + bp.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wrappedBean;
    }

    @Override
    public void preInstantiateSingletons() throws RuntimeException {
        String[] beanDefinitionNames = getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition bd = getBeanDefinition(beanDefinitionName);
            if (!bd.isLazyInit() && !bd.isAbstract() && bd.isSingleton()){
                getBean(beanDefinitionName);
            }
        }
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        List<String> ans = new ArrayList<>();

        String[] beanDefinitionNames = getBeanDefinitionNames();
        try {
            for (String beanDefinitionName : beanDefinitionNames) {
                BeanDefinition bd = getBeanDefinition(beanDefinitionName);
                if (bd.isAbstract()){
                    continue;
                }
                if (bd.getBeanClass() == type && !ans.contains(beanDefinitionName)){
                    ans.add(beanDefinitionName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return StringUtils.toStringArray(ans);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception {
        Map<String, T> ans = new LinkedHashMap<>();
        String[] beanNames = getBeanNamesForType(type);
        for (String beanName : beanNames) {
            Object bean = getBean(beanName);
            if (bean != null){
                ans.put(beanName, (T) bean);
            }
        }
        return ans;
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        Map<String, Object> ans = new LinkedHashMap<>();
        String[] beanDefinitionNames = getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition bd = getBeanDefinition(beanDefinitionName);
            if (bd != null && !bd.isAbstract() && findAnnotationOnBean(beanDefinitionName, annotationType) != null){
                Object bean = getBean(beanDefinitionName);
                if (bean != null){
                    ans.put(beanDefinitionName, bean);
                }
            }
        }
        return ans;
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanDefName, Class<A> annotationType) throws Exception {
        if (containsBeanDefinition(beanDefName)){
            BeanDefinition beanDefinition = getBeanDefinition(beanDefName);
            Class<?> beanClass = beanDefinition.getBeanClass();
            Annotation[] annotations = beanClass.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == annotationType){
                    return (A) annotation;
                }
            }
        }
        return null;
    }
}
