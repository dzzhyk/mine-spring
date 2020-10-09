package com.yankaizhang.springframework.context;

import com.yankaizhang.springframework.annotation.Autowired;
import com.yankaizhang.springframework.annotation.Controller;
import com.yankaizhang.springframework.annotation.Component;
import com.yankaizhang.springframework.annotation.Service;
import com.yankaizhang.springframework.aop.AopConfig;
import com.yankaizhang.springframework.aop.AopProxy;
import com.yankaizhang.springframework.aop.CglibAopProxy;
import com.yankaizhang.springframework.aop.JdkDynamicAopProxy;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import com.yankaizhang.springframework.beans.BeanWrapper;
import com.yankaizhang.springframework.beans.config.BeanDefinition;
import com.yankaizhang.springframework.beans.config.BeanPostProcessor;
import com.yankaizhang.springframework.beans.support.BeanDefinitionReader;
import com.yankaizhang.springframework.context.support.DefaultListableBeanFactory;
import com.yankaizhang.springframework.core.BeanFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 真正使用到的IOC容器，直接接触用户
 * 继承了DefaultListableBeanFactory，并且把里面的refresh方法实现了
 * 实现了BeanFactory接口，实现了getBean()方法
 * 完成IoC、DI、AOP的衔接
 */
@SuppressWarnings("all")
@Slf4j
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    private String[] configLocations;
    private BeanDefinitionReader reader;

    /**
     * 单例的IoC容器
     */
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * 通用的IoC容器
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<>();

    public ApplicationContext(String... configLocations){
        this.configLocations = configLocations;
        try {
            refresh();  // 调用自己重写的refresh，多态
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() throws Exception {
        // 1. 定位配置文件
        reader = new BeanDefinitionReader(this.configLocations);
        // 2. 加载配置文件，扫描类，把他们封装成BeanDefinition
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // 3. 注册信息
        doRegisterBeanDefinition(beanDefinitions);
        // 4. 将所有bean定义实例化
        doInstance();
        // 5. 对当前bean依赖注入
        doAutowired();
    }



    /**
     * 注册BeanDefinition
     */
    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception{
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("IoC容器已经存在：" + beanDefinition.getFactoryBeanName() + "类的bean定义了！不要重复加载bean定义");
            }
            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    /**
     * 把非懒加载的类提前初始化
     */
    private void doInstance() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            // 如果不是懒加载，初始化bean
            if (!beanDefinitionEntry.getValue().isLazyInit()){
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 依赖注入
     */
    private void doAutowired(){

        for (Map.Entry<String, BeanWrapper> entry : factoryBeanInstanceCache.entrySet()) {
            Object instance = entry.getValue().getWrappedInstance();
            // 依赖注入
            populateBean(entry.getKey(), instance);
        }
    }

    /**
     * 根据不同的注解类型在beanDefinitionMap中选择bean初始化
     */
    private void getTypeBean(Class<?> type){

        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            // 如果不是懒加载，初始化bean
            if (!beanDefinitionEntry.getValue().isLazyInit()){
                try {
                    Class<?> clazz = Class.forName(beanDefinitionEntry.getValue().getBeanClassName());
                    if (clazz.isAnnotationPresent((Class<? extends Component>) type)){
                        // 如果是注解定义的组件
                        getBean(beanName);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * bean实例化过程
     * 1. 保存原来的OOP对象依赖关系
     * 2. 利用装饰器BeanWrapper，扩展增强这个类，方便AOP操作
     */
    @Override
    public Object getBean(String beanName) throws Exception {

        // 检查是否已经有了实例化好的bean
        if (factoryBeanInstanceCache.containsKey(beanName)){
            return factoryBeanInstanceCache.get(beanName).getWrappedInstance();
        }

        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        try {
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) return null;

            // 前置处理
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            // 生成BeanWrapper增强对象
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            this.factoryBeanInstanceCache.put(beanName, beanWrapper);

            // 后置处理
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            // 返回实例化完成的bean，等待注入
            return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }


    /**
     * 对bean实例属性进行依赖注入
     */
    private void populateBean(String beanName, Object instance){
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(Controller.class)) ||
            clazz.isAnnotationPresent(Service.class)){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;
            Autowired autowired = field.getAnnotation(Autowired.class);

            // 获取待注入bean的beanName
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                autowiredBeanName = BeanDefinitionReader.toLowerCase(field.getType().getSimpleName());
            }

            field.setAccessible(true);
            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 实例化Bean定义
     * 传入bean定义，返回bean实例
     */
    private Object instantiateBean(BeanDefinition beanDefinition) throws Exception {
        Object instance = null;

        String beanClassName = beanDefinition.getBeanClassName();   // 使用beanClassName实例化对象
        String factoryBeanName = beanDefinition.getFactoryBeanName();   // 实际id
        try {
            if (this.factoryBeanObjectCache.containsKey(beanClassName)){
                // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                instance = this.factoryBeanObjectCache.get(beanClassName);
            }else {
                Class<?> clazz = Class.forName(beanClassName);
                instance = clazz.newInstance();



                // AOP支持
                AdvisedSupport config = instantiateAopConfig(beanDefinition);
                config.setTargetClass(clazz);   // 设置目标类
                config.setTarget(instance);
                if (config.pointCutMatch()){
                    // 如果切点表达式命中了，说明这个实例中有方法需要被切入
                    log.debug("切点表达式命中于 ==> " + config.getTargetClass());
                    instance = createProxy(config).getProxy();
                }

                this.factoryBeanObjectCache.put(beanClassName, instance);
            }
            return instance;
        }catch (InstantiationException e){
            throw new Exception("bean实例化错误：" + beanClassName);
        }
    }


    /**
     * 加载aop配置，返回包装类
     */
    private AdvisedSupport instantiateAopConfig(BeanDefinition beanDefinition) throws Exception{
        AopConfig aopConfig = new AopConfig();

        aopConfig.setPointCut(reader.getConfig().getProperty("pointCut"));
        aopConfig.setAspectClass(reader.getConfig().getProperty("aspectClass"));
        aopConfig.setAspectBefore(reader.getConfig().getProperty("aspectBefore"));
        aopConfig.setAspectAfter(reader.getConfig().getProperty("aspectAfter"));
        aopConfig.setAspectAfterThrow(reader.getConfig().getProperty("aspectAfterThrow"));
        aopConfig.setAspectAfterThrowingName(reader.getConfig().getProperty("aspectAfterThrowingName"));
        return new AdvisedSupport(aopConfig);
    }

    /**
     * 创建代理对象
     */
    private AopProxy createProxy(AdvisedSupport config){
        if (config.getTargetClass().getInterfaces().length > 0){
            return new JdkDynamicAopProxy(config);  // 如果对象存在接口，默认使用jdk动态代理
        }
        return new CglibAopProxy(config);       // 如果对象没有接口，使用CGLib动态代理
    }


    /**
     * 获取容器中bean定义名称列表
     */
    public String[] getBeanDefinitionNames(){
        return this.beanDefinitionMap.keySet().toArray(new String[0]);
    }

    /**
     * 获取容器中bean定义数量
     */
    public int getBeanDefinitionCount(){
        return this.beanDefinitionMap.size();
    }

    /**
     * 获取配置信息
     */
    public Properties getConfig(){
        return this.reader.getConfig();
    }

    public Map<String, Object> getFactoryBeanObjectCache() {
        return factoryBeanObjectCache;
    }

    public Map<String, BeanWrapper> getFactoryBeanInstanceCache() {
        return factoryBeanInstanceCache;
    }
}
