package com.yankaizhang.spring.beans.factory.support;

import com.yankaizhang.spring.beans.BeanDefinition;
import com.yankaizhang.spring.beans.factory.BeanFactory;
import com.yankaizhang.spring.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * BeanFactory的一个简易实现
 * TODO: 添加中间层实现，拆分这个类的功能
 * @author dzzhyk
 * @since 2020-12-20 18:19:33
 */
public class DefaultListableBeanFactory
        implements BeanFactory, BeanDefinitionRegistry, Serializable {

    private static final Logger log = LoggerFactory.getLogger(DefaultListableBeanFactory.class);

    /** beanDefinitionMap，用来存储注册信息，当做bean定义的缓存 */
    protected final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /** bean定义名称的列表 */
    protected volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    /** 是否允许覆盖注册同名的beanDefinition */
    private boolean allowBeanDefinitionOverriding = true;

    public DefaultListableBeanFactory() {
        super();
    }

    @Override
    public Object getBean(String beanName) throws Exception {
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }

    @Override
    public Object getBean(String beanName, Class<?> beanClass) throws Exception {
        return null;
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
        Assert.hasText(beanName, "beanName不能为null");
        Assert.notNull(beanDefinition, "bean定义不能为null");
        BeanDefinition existedDefinition = this.beanDefinitionMap.get(beanName);

        if (existedDefinition != null){

            if (isAllowBeanDefinitionOverriding()){
                log.warn("覆盖注册了bean定义 : " + beanName);
                this.beanDefinitionMap.put(beanName, beanDefinition);
            }else{
                throw new Exception("不允许重复注册bean定义 => " + beanName);
            }

        }else{
            this.beanDefinitionMap.put(beanName, beanDefinition);
            this.beanDefinitionNames.add(beanName);
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
    public BeanDefinition getBeanDefinition(String beanName) throws Exception {
        Assert.hasText(beanName, "beanName不能为null");
        BeanDefinition definition = this.beanDefinitionMap.get(beanName);
        if (definition == null){
            throw new Exception("未找到bean定义 => " + beanName);
        }
        return definition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
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
}
