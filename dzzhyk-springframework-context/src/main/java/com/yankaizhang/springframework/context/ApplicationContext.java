package com.yankaizhang.springframework.context;

import com.yankaizhang.springframework.aop.aopanno.Aspect;
import com.yankaizhang.springframework.aop.aopanno.PointCut;
import com.yankaizhang.springframework.aop.AopConfig;
import com.yankaizhang.springframework.aop.AopProxy;
import com.yankaizhang.springframework.aop.CglibAopProxy;
import com.yankaizhang.springframework.aop.JdkDynamicAopProxy;
import com.yankaizhang.springframework.aop.support.AdviceSupportComparator;
import com.yankaizhang.springframework.aop.support.AdvisedSupport;
import com.yankaizhang.springframework.aop.support.AopAnnotationReader;
import com.yankaizhang.springframework.beans.BeanWrapper;
import com.yankaizhang.springframework.beans.factory.BeanFactory;
import com.yankaizhang.springframework.beans.factory.annotation.Autowired;
import com.yankaizhang.springframework.beans.factory.config.BeanDefinitionReader;
import com.yankaizhang.springframework.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.springframework.beans.factory.support.BeanDefinition;
import com.yankaizhang.springframework.context.annotation.Controller;
import com.yankaizhang.springframework.context.annotation.Service;
import com.yankaizhang.springframework.context.support.DefaultListableBeanFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 真正使用到的IOC容器，直接接触用户
 * 继承了DefaultListableBeanFactory，并且把里面的refresh方法实现了
 * 实现了BeanFactory接口，实现了getBean()方法
 * 完成IoC、DI、AOP的衔接
 */
@SuppressWarnings("all")
public class ApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    public static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ApplicationContext.class);

    private String[] configLocations;
    private BeanDefinitionReader reader;
    private AopAnnotationReader aopAnnotationReader;    // aop注解reader

    /**
     * 单例的IoC容器
     */
    private Map<String, Object> factoryBeanObjectCacheByName = new ConcurrentHashMap<>();
    private Map<String, Object> factoryBeanObjectCacheByType = new ConcurrentHashMap<>();

    /**
     * 通用的IoC容器
     */
    private Map<String, BeanWrapper> factoryBeanInstanceCacheByName = new ConcurrentHashMap<>();    // id容器
    private Map<String, BeanWrapper> factoryBeanInstanceCacheByType = new ConcurrentHashMap<>();    // type容器

    /**
     * 通用的AOP切面容器
     */
    private List<Class<?>> aspectBeanInstanceCache = new CopyOnWriteArrayList<>();


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
        // 5. 处理AOP对象
        doAop();
        // 6. 处理依赖注入
        doAutowired();
    }

    /**
     * 处理Aop切面
     */
    public void doAop() throws Exception {

        postProcessAspects();

        List<AdvisedSupport> aopConfigs = aopAnnotationReader.parseAspect();    // 解析所有已知切面类，获取包装类的config

        if (aopConfigs != null){

            // 排序aopConfigs，按照类别深度从深到浅
            aopConfigs.sort(new AdviceSupportComparator());

            // 转变每个aopConfig为多个，目标类分别对应目录下的类
            for (AdvisedSupport aopConfig : aopConfigs) {
                List<String> targetClassList = aopConfig.parseClasses();    // 获取这个切面可以切到的目标类
                for (String clazzName : targetClassList) {
                    if (factoryBeanInstanceCacheByType.containsKey(clazzName)){
                        // 只处理容器中有的组件
                        BeanWrapper beanWrapper = factoryBeanInstanceCacheByType.get(clazzName);
                        Object wrappedInstance = beanWrapper.getWrappedInstance();
                        Class<?> wrappedClass = beanWrapper.getWrappedClass();  // 虽然可能是代理类，但是Class一定是代理类的最终目标类

                        // 这里将原有的aopConfig为每个代理类扩增，防止java内存赋值
                        AdvisedSupport myConfig = getProxyAopConfig(aopConfig, wrappedInstance, wrappedClass);
//                        aopConfig.setTarget(wrappedInstance);
//                        aopConfig.setTargetClass(wrappedClass);

                        if (myConfig.pointCutMatch()){
                            Object proxy = createProxy(myConfig).getProxy();
                            beanWrapper.setWrappedInstance(proxy);  // 将这个二次代理对象包装起来

                            log.debug("创建代理对象 ==> " + proxy.getClass());
                            factoryBeanInstanceCacheByType.replace(clazzName, beanWrapper);       // 重新设置对象
                            String beanName = BeanDefinitionReader.toLowerCase(clazzName.substring(clazzName.lastIndexOf(".") + 1));
                            factoryBeanInstanceCacheByName.replace(beanName, beanWrapper);    // 同时更新Name空间

                            // 如果这个类有接口，同时更新这些接口的实现类对象
                            for (Class<?> anInterface : wrappedClass.getInterfaces()) {
                                String interfaceName = anInterface.getName();
                                String iocBeanInterfaceName = BeanDefinitionReader.toLowerCase(interfaceName.substring(interfaceName.lastIndexOf(".") + 1));
                                factoryBeanInstanceCacheByName.replace(iocBeanInterfaceName, beanWrapper);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据已有的aopConfig得到新的aopConfig实例，同时设置TargetClass
     */
    private AdvisedSupport getProxyAopConfig(AdvisedSupport advisedSupport, Object wrappedInstance, Class<?> wrappedClass)
            throws Exception {
        AopConfig myAopConfig = advisedSupport.getAopConfig().clone();
        AdvisedSupport myAdviceSupport = new AdvisedSupport(myAopConfig);
        myAdviceSupport.setTarget(wrappedInstance);
        myAdviceSupport.setTargetClass(wrappedClass);
        return myAdviceSupport;
    }

    /*
        预处理所有已知切面类，收集切面表达式，创建AopAnnotationReader对象
     */
    private void postProcessAspects() {

        HashMap<Class<?>, String> map = new HashMap<>();

        for (Class<?> aspectClazz : aspectBeanInstanceCache) {

            Method[] aspectClazzMethods = aspectClazz.getDeclaredMethods();

            // 加载切点类PointCut
            List<Method> aspectMethods = new ArrayList<>(aspectClazzMethods.length);
            Collections.addAll(aspectMethods, aspectClazzMethods);

            for (Method method : aspectMethods) {
                if (method.isAnnotationPresent(PointCut.class)){
                    // 如果定义了切点类，保存切点表达式
                    String execution = method.getAnnotation(PointCut.class).value().trim();
                    map.put(aspectClazz, execution);
                    break;
                }
            }
        }
        aopAnnotationReader = new AopAnnotationReader();
        aopAnnotationReader.setPointCutMap(map);
    }


    /**
     * 注册BeanDefinition
     */
    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) throws Exception{
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (super.beanDefinitionMapByName.containsKey(beanDefinition.getFactoryBeanName())){
                throw new Exception("IoC容器已经存在：" + beanDefinition.getFactoryBeanName() + "类的bean定义了！不要重复加载bean定义");
            }
            super.beanDefinitionMapByName.put(beanDefinition.getFactoryBeanName(), beanDefinition);
            super.beanDefinitionMapByType.put(beanDefinition.getBeanClassName(), beanDefinition);
        }
    }

    /**
     * 把非懒加载的类提前初始化
     */
    private void doInstance() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : super.beanDefinitionMapByName.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            // 如果不是懒加载，初始化bean
            if (!beanDefinition.isLazyInit()){
                try {
                    getBean(beanName);  // name
                    getBean(beanDefinition.getBeanClassName()); // type
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 依赖注入
     */
    private void doAutowired() throws Exception {

        for (Map.Entry<String, BeanWrapper> entry : factoryBeanInstanceCacheByName.entrySet()) {
            Object instance = entry.getValue().getWrappedInstance();
            // 依赖注入
            populateBean(entry.getKey(), instance);
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
        if (factoryBeanInstanceCacheByName.containsKey(beanName)){
            return factoryBeanInstanceCacheByName.get(beanName).getWrappedInstance();
        }

        if (factoryBeanInstanceCacheByType.containsKey(beanName)){
            return factoryBeanInstanceCacheByType.get(beanName).getWrappedInstance();
        }

        boolean flag = false;   // 判断是通过名字找到的还是通过类型找到的
        BeanDefinition beanDefinition = super.beanDefinitionMapByName.get(beanName);
        if (null == beanDefinition){
            beanDefinition = super.beanDefinitionMapByType.get(beanName);
            flag = true;
        }
        if (null == beanDefinition){
            throw new Exception("bean定义在Map中未找到 ==> " + beanName);
        }


        try {
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) return null;

            // 前置处理
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            // 生成BeanWrapper增强对象
            BeanWrapper beanWrapper = new BeanWrapper(instance);
            if (!flag)
                this.factoryBeanInstanceCacheByName.put(beanName, beanWrapper);
            else
                this.factoryBeanInstanceCacheByType.put(beanName, beanWrapper);

            // 后置处理
            beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            // 返回实例化完成的bean，等待注入
            return beanWrapper.getWrappedInstance();

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
    private void populateBean(String beanName, Object instance) throws Exception{
        Class<?> clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(Controller.class) ||
            clazz.isAnnotationPresent(Service.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) continue;
            Autowired autowired = field.getAnnotation(Autowired.class);

            // 获取待注入bean的beanName   - 改为全类名
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)){
                // 如果没有，找到这个注解标记的属性的全类名，注入全类名
                Class<?> type = field.getType();
                if (type.isInterface()){
                    // 如果是接口类型，注入其实现类，通过ByName在nameCache里面找他的实现类
                    autowiredBeanName = BeanDefinitionReader.toLowerCase(type.getSimpleName());
                }else{
                    autowiredBeanName = type.getName();
                }
            }

            field.setAccessible(true);
            BeanWrapper toAutowiredInstanceWrapper = null;
            // 默认使用name进行自动装配
            toAutowiredInstanceWrapper = this.factoryBeanInstanceCacheByName.get(autowiredBeanName);

            if (null == toAutowiredInstanceWrapper){
                // 如果没有找到按照name装配的bean，寻找按照type装配的bean
                toAutowiredInstanceWrapper = this.factoryBeanInstanceCacheByType.get(autowiredBeanName);
            }
            if (null == toAutowiredInstanceWrapper){
                throw new Exception("IoC容器未找到相应的bean对象 ==> " + autowiredBeanName);
            }
            Object wrappedInstance = toAutowiredInstanceWrapper.getWrappedInstance();
            if (null == wrappedInstance){
                throw new Exception("BeanWrapper代理instance对象不存在 ==> " + autowiredBeanName);
            }
            field.set(instance, wrappedInstance);
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
            if (this.factoryBeanObjectCacheByType.containsKey(beanClassName)){
                // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                instance = this.factoryBeanObjectCacheByType.get(beanClassName);
            } else {
                Class<?> clazz = Class.forName(beanClassName);
                instance = clazz.newInstance();
                // 注解AOP支持
                if (clazz.isAnnotationPresent(Aspect.class)){
                    // 如果是切面类，加入切面容器
                    aspectBeanInstanceCache.add(clazz);
                }
                this.factoryBeanObjectCacheByType.put(beanClassName, instance);
                this.factoryBeanObjectCacheByName.put(factoryBeanName, instance);
            }
            return instance;
        }catch (InstantiationException e){
            throw new Exception("bean实例化错误：" + beanClassName);
        }
    }


    /**
     * 加载aop配置，返回包装类
     */
    private AdvisedSupport instantiateAopConfig() throws Exception{
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
     * 加载基于注解的aop配置，返回包装类
     */
//    private List<AdvisedSupport> instantiateAopAnnotationConfig(Class<?> clazz){
//        AopConfig aopConfig = aopAnnotationReader.parseAspect(clazz);
//        return new AdvisedSupport(aopConfig);
//    }

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
        return this.beanDefinitionMapByName.keySet().toArray(new String[0]);
    }

    /**
     * 获取容器中按照name对应的bean定义数量
     */
    public int getBeanDefinitionCountByName(){
        return this.beanDefinitionMapByName.size();
    }

    /**
     * 获取容器中按照type对应的bean定义数量
     */
    public int getBeanDefinitionCountByType(){
        return this.beanDefinitionMapByType.size();
    }

    /**
     * 获取配置信息
     */
    public Properties getConfig(){
        return this.reader.getConfig();
    }

    public Map<String, Object> getFactoryBeanObjectCacheByName() {
        return factoryBeanObjectCacheByName;
    }

    public Map<String, Object> getFactoryBeanObjectCacheByType() {
        return factoryBeanObjectCacheByType;
    }

    public Map<String, BeanWrapper> getFactoryBeanInstanceCacheByName() {
        return factoryBeanInstanceCacheByName;
    }

    public Map<String, BeanWrapper> getFactoryBeanInstanceCacheByType() {
        return factoryBeanInstanceCacheByType;
    }
}
