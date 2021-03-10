package com.yankaizhang.spring.context.support;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanDefinitionRegistryPostProcessor;
import com.yankaizhang.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.beans.factory.impl.DefaultBeanFactory;
import com.yankaizhang.spring.context.ApplicationContext;
import com.yankaizhang.spring.context.config.ConfigurableApplicationContext;
import com.yankaizhang.spring.context.config.ConfigurationClassPostProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 上下文容器实现类的顶层抽象类
 * 实现了通用的公共方法
 * @author dzzhyk
 * @since 2021-03-10 02:14:08
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

    private static final Logger log = LoggerFactory.getLogger(AbstractApplicationContext.class);

    private ApplicationContext parent;

    /** beanFactoryPostProcessor列表 */
    private List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    public AbstractApplicationContext() {
        // 加入配置类处理器
        beanFactoryPostProcessors.add(new ConfigurationClassPostProcessor());
    }

    /**
     * 初始化或刷新容器内容
     */
    @Override
    public void refresh() throws RuntimeException {

        try {
            // 获取工厂对象
            DefaultBeanFactory beanFactory = (DefaultBeanFactory) getBeanFactory();

            // 执行beanFactory的前置处理器
            invokeBeanFactoryPostProcessors(beanFactory);

            // 在执行所有bean创建之前，先创建所有bean对象处理器
            registerBeanPostProcessors(beanFactory);

            // 初始化其他非懒加载的bean对象
            finishBeanFactoryInitialization(beanFactory);

        }catch (Exception e){
            log.debug("执行refresh方法失败 => " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

    /**
     * 向容器中注册bean处理器
     * @param beanFactory bean容器
     */
    private void registerBeanPostProcessors(CompletedBeanFactory beanFactory) {

        String[] beanPostProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class);
        for (String processorName : beanPostProcessorNames) {
            BeanPostProcessor processor = beanFactory.getBean(processorName, BeanPostProcessor.class);
            beanFactory.addBeanPostProcessor(processor);
        }

    }

    /**
     * 完成bean容器的其他对象初始化
     * @param beanFactory bean容器
     */
    private void finishBeanFactoryInitialization(DefaultBeanFactory beanFactory) {
        beanFactory.preInstantiateSingletons();
    }

    /**
     * 执行beanFactory的前置处理器<br/>
     * {@link BeanFactoryPostProcessor}与{@link BeanDefinitionRegistryPostProcessor}
     * @param beanFactory bean容器
     */
    private void invokeBeanFactoryPostProcessors(CompletedBeanFactory beanFactory) {
        List<BeanFactoryPostProcessor> remainedProcessors = new ArrayList<>(beanFactoryPostProcessors);
        if (beanFactory instanceof BeanDefinitionRegistry){
            try {
                for (BeanFactoryPostProcessor bfp : beanFactoryPostProcessors) {
                    if (bfp instanceof BeanDefinitionRegistryPostProcessor){
                        ((BeanDefinitionRegistryPostProcessor) bfp)
                                .postProcessBeanDefinitionRegistry((BeanDefinitionRegistry) beanFactory);
                        remainedProcessors.remove(bfp);
                        log.debug("执行BeanDefinitionRegistryPostProcessor : " + bfp.getClass().toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 执行剩余的beanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(remainedProcessors, beanFactory);
    }

    /**
     * 执行BeanFactoryPostProcessor方法
     */
    private void invokeBeanFactoryPostProcessors(
            List<? extends BeanFactoryPostProcessor> postProcessors, CompletedBeanFactory beanFactory) {
        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
            log.debug("执行BeanFactoryPostProcessor : " + postProcessor.getClass().toString());
        }
    }

    public ApplicationContext getParent() {
        return this.parent;
    }

    public void setParent(ApplicationContext context){
        this.parent = context;
    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return getParent();
    }

    @Override
    public Object getBean(String beanName) throws RuntimeException {
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> beanClass) throws RuntimeException {
        return getBeanFactory().getBean(beanClass);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanClass) throws RuntimeException {
        return getBeanFactory().getBean(beanName, beanClass);
    }

    @Override
    public boolean containsBean(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) throws Exception {
        return getBeanFactory().isSingleton(beanName);
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> annotationType) throws Exception {
        return getBeanFactory().findAnnotationOnBean(beanName, annotationType);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws Exception {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) throws Exception {
        return getBeanFactory().getBeansWithAnnotation(annotationType);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return beanFactoryPostProcessors;
    }
}
