package com.yankaizhang.spring.beans.factory.impl;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.CompletedBeanFactory;
import com.yankaizhang.spring.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.spring.beans.factory.support.AbstractCompletedBeanFactory;
import com.yankaizhang.spring.beans.BeanDefinitionRegistry;
import com.yankaizhang.spring.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 集成所有功能（遍历、配置、自动装配）的默认实现bean工厂
 * @author dzzhyk
 * @since 2020-12-21 18:37:52
 */
public class DefaultBeanFactory extends AbstractCompletedBeanFactory
        implements CompletedBeanFactory, BeanDefinitionRegistry, Serializable {

    private static final Logger log = LoggerFactory.getLogger(DefaultBeanFactory.class);

    /** beanDefinitionMap，用来存储注册信息，当做bean定义的缓存 */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** bean定义名称的列表 */
    protected volatile List<String> beanDefinitionNames = new LinkedList<>();

    /** 是否允许覆盖注册同名的beanDefinition */
    private boolean allowBeanDefinitionOverriding = true;

    public DefaultBeanFactory() {
        super();
    }

    @Override
    public void registerBeanDefinition(String beanDefName, BeanDefinition beanDefinition) throws Exception {
        Assert.hasText(beanDefName, "beanDefName不能为null");
        Assert.notNull(beanDefinition, "bean定义不能为null");
        BeanDefinition existedDefinition = this.beanDefinitionMap.get(beanDefName);

        if (existedDefinition != null){

            if (isAllowBeanDefinitionOverriding()){
                log.warn("覆盖注册了bean定义 : " + beanDefName);
                this.beanDefinitionMap.put(beanDefName, beanDefinition);
            }else{
                throw new Exception("不允许重复注册bean定义 => " + beanDefName);
            }

        }else{
            this.beanDefinitionMap.put(beanDefName, beanDefinition);
            this.beanDefinitionNames.add(beanDefName);
            log.debug("注册bean定义 : ["+ beanDefName + "] => " + beanDefinition.getBeanClassName());
        }

    }

    @Override
    public void removeBeanDefinition(String beanName) throws Exception {
        Assert.hasText(beanName, "beanName不能为null");
        BeanDefinition definition = this.beanDefinitionMap.remove(beanName);
        if (definition == null){
            throw new Exception("未找到准备删除的bean定义 => " + beanName);
        }
        this.beanDefinitionNames.remove(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionNames.toArray(new String[0]);
    }

    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return this.beanDefinitionNames.contains(beanName);
    }

    public boolean isAllowBeanDefinitionOverriding() {
        return allowBeanDefinitionOverriding;
    }

    public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding) {
        this.allowBeanDefinitionOverriding = allowBeanDefinitionOverriding;
    }

    /*
        必须实现的来自AbstractConfigurableBeanFactory的抽象方法
     */

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanDefName) throws RuntimeException {
        Assert.hasText(beanDefName, "beanDefName不能为null");
        BeanDefinition definition = this.beanDefinitionMap.get(beanDefName);
        if (definition == null){
            throw new RuntimeException("beanDefinitionMap中未找到bean定义 => " + beanDefName);
        }
        return definition;
    }

    @Override
    public Map<String, BeanDefinition> getBeanDefinitionMap() {
        return beanDefinitionMap;
    }
}
