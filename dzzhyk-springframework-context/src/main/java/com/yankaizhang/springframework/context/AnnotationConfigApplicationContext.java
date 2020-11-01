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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * AnnotationConfigApplicationContext是用来解析注解配置类的容器对象
 */
@SuppressWarnings("all")
public class AnnotationConfigApplicationContext extends DefaultListableBeanFactory implements BeanFactory {

    public static final Logger log = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private String[] configLocations;
    private BeanDefinitionReader reader;
    private AopAnnotationReader aopAnnotationReader;    // aop注解reader

    /**
     * 单例IoC容器
     * 这个容器一般存放扫描到的Bean单例类对象
     */
    private Map<String, Object> singletonIoc = new ConcurrentHashMap<>();

    /**
     * 通用的IoC容器
     * 我们最终使用的一般是这个通用的IoC容器
     * 这个容器中的所有Bean对象应该都是经过增强的包装Bean
     */
    private Map<String, BeanWrapper> commonIoc = new ConcurrentHashMap<>();

    /**
     * 通用的AOP切面容器
     * TODO: 这个容器不应该存在，但是还没有想出很好的办法
     */
    private List<Class<?>> aspectBeanInstanceCache = new CopyOnWriteArrayList<>();


    public AnnotationConfigApplicationContext(String... configLocations){
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
        // 5. 处理AOP对象x
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
                    if (commonIoc.containsKey(clazzName)){
                        // 只处理容器中有的组件
                        BeanWrapper beanWrapper = commonIoc.get(clazzName);
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
                            commonIoc.replace(clazzName, beanWrapper);       // 重新设置commonIoC中的对象为代理对象
                            String beanName = BeanDefinitionReader.toLowerCase(clazzName.substring(clazzName.lastIndexOf(".") + 1));
                            commonIoc.replace(beanName, beanWrapper);    // 同时更新beanName对应的实例

                            // 如果这个类有接口，同时更新这些接口的实现类对象
                            for (Class<?> anInterface : wrappedClass.getInterfaces()) {
                                String interfaceName = anInterface.getName();
                                String iocBeanInterfaceName = BeanDefinitionReader.toLowerCase(interfaceName.substring(interfaceName.lastIndexOf(".") + 1));
                                commonIoc.replace(iocBeanInterfaceName, beanWrapper);   // 对于接口，只需要更新其beanName对应的实例，因为beanClass对应的实例已经更新过了
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

    /**
     * 预处理所有已知切面类，收集切面表达式，创建AopAnnotationReader对象
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
            String beanName = beanDefinitionEntry.getKey(); // 首字母小写的类名
            BeanDefinition beanDefinition = beanDefinitionEntry.getValue();
            // 如果不是懒加载，初始化bean
            if (!beanDefinition.isLazyInit()){
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
    private void doAutowired() throws Exception {

        for (Map.Entry<String, BeanWrapper> entry : commonIoc.entrySet()) {
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
        if (commonIoc.containsKey(beanName)){
            return commonIoc.get(beanName).getWrappedInstance();
        }

        BeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);
        if (null == beanDefinition){
            throw new Exception("bean定义在Map中未找到，检查beanName是否有误 ==> " + beanName);
        }


        try {
            BeanPostProcessor beanPostProcessor = new BeanPostProcessor();
            Object instance = instantiateBean(beanDefinition);
            if (null == instance) return null;

            // 前置处理
            beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            // 生成BeanWrapper增强对象
            BeanWrapper beanWrapper = new BeanWrapper(instance);

            this.commonIoc.put(beanName, beanWrapper);  // beanName可以找到这个实例
            this.commonIoc.putIfAbsent(beanDefinition.getBeanClassName(), beanWrapper); // 全类名也可以找到这个实例

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
        return getBean(reader.toLowerCase(beanClass.getSimpleName()));
    }

    @Override
    public Object getBean(String beanName, Class<?> beanClass) throws Exception {
        return null;
    }


    /**
     * 对bean实例属性进行依赖注入
     * 目前只支持注入Service和Controller类型
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
                autowiredBeanName = BeanDefinitionReader.toLowerCase(type.getSimpleName());
            }

            field.setAccessible(true);
            BeanWrapper toAutowiredInstanceWrapper = null;
            // 默认使用name进行自动装配
            toAutowiredInstanceWrapper = this.commonIoc.get(autowiredBeanName);

            if (null == toAutowiredInstanceWrapper){
                // 如果没有找到按照name装配的bean，寻找按照type装配的bean
                autowiredBeanName = field.getName();
                toAutowiredInstanceWrapper = this.commonIoc.get(autowiredBeanName);
            }
            if (null == toAutowiredInstanceWrapper){
                throw new Exception("commonsIoC容器未找到相应的bean对象 ==> " + autowiredBeanName);
            }
            Object wrappedInstance = toAutowiredInstanceWrapper.getWrappedInstance();
            if (null == wrappedInstance){
                throw new Exception("BeanWrapper代理instance对象不存在 ==> " + autowiredBeanName);
            }

            // 将获取的包装类对象装配上去
            field.set(instance, wrappedInstance);
        }
    }


    /**
     * 实例化Bean定义
     * 实例化的结果是单例IoC中的Bean对象
     * com.yankaizhang.test.service.impl.TestServiceImpl -> TestServiceImpl@1024
     */
    private Object instantiateBean(BeanDefinition beanDefinition) throws Exception {
        Object instance = null;

        String beanClassName = beanDefinition.getBeanClassName();   // 使用beanClassName实例化对象
        try {
            if (this.singletonIoc.containsKey(beanClassName)){
                // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                instance = this.singletonIoc.get(beanClassName);
            } else {
                Class<?> clazz = Class.forName(beanClassName);
                instance = clazz.newInstance();
                // 注解AOP支持
                if (clazz.isAnnotationPresent(Aspect.class)){
                    // 如果是切面类，加入切面容器
                    aspectBeanInstanceCache.add(clazz);
                }
                this.singletonIoc.put(beanClassName, instance); // 加入singletonIoc
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
     * 获取容器中按照name对应的bean定义数量
     */
    public int getBeanDefinitionCountByName(){
        return this.beanDefinitionMap.size();
    }

    /**
     * 获取配置信息
     */
    public Properties getConfig(){
        return this.reader.getConfig();
    }


    public Map<String, Object> getSingletonIoc() {
        return singletonIoc;
    }

    public Map<String, BeanWrapper> getCommonIoc() {
        return commonIoc;
    }
}
