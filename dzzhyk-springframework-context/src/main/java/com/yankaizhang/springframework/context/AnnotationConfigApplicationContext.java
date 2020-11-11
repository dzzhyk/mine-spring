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
import com.yankaizhang.springframework.aop.support.AopUtils;
import com.yankaizhang.springframework.beans.BeanDefinition;
import com.yankaizhang.springframework.beans.BeanWrapper;
import com.yankaizhang.springframework.beans.factory.BeanFactory;
import com.yankaizhang.springframework.beans.factory.annotation.Autowired;
import com.yankaizhang.springframework.context.annotation.*;
import com.yankaizhang.springframework.context.config.AnnotatedBeanDefinitionReader;
import com.yankaizhang.springframework.beans.factory.config.BeanPostProcessor;
import com.yankaizhang.springframework.context.config.ClassPathBeanDefinitionScanner;
import com.yankaizhang.springframework.context.config.ConfigClassReader;
import com.yankaizhang.springframework.context.support.DefaultListableBeanFactory;
import com.yankaizhang.springframework.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.yankaizhang.springframework.util.StringUtils.toLowerCase;

/**
 * 真正使用到的IOC容器，直接接触用户
 * 继承了DefaultListableBeanFactory，并且把里面的refresh方法实现了
 * 实现了BeanFactory接口，实现了getBean()方法
 * 完成IoC、DI、AOP的衔接
 * AnnotationConfigApplicationContext是专门用来解析注解配置类的容器对象
 *
 * 实现了AnnotationConfigRegistry接口，说明拥有基本的两个方法scan和register
 *
 * TODO: 这里其实对于AnnotationConfigApplicationContext而言，在这一层实现了多个功能，其实是简化过后的
 *
 * @author dzzhyk
 */
@SuppressWarnings("all")
public class AnnotationConfigApplicationContext extends DefaultListableBeanFactory
        implements BeanFactory, AnnotationConfigRegistry, BeanDefinitionRegistry {

    public static final Logger log = LoggerFactory.getLogger(AnnotationConfigApplicationContext.class);

    private AnnotatedBeanDefinitionReader reader;
    private ClassPathBeanDefinitionScanner scanner;

    /**
     * 配置类解析器
     * TODO: 这个应该使用前置处理器机制替代
     */
    private ConfigClassReader configClassReader;
    /**
     * aop注解reader
     * TODO: 目前还没有更好的替代方案
     */
    private AopAnnotationReader aopAnnotationReader;

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



    public AnnotationConfigApplicationContext() {
        this.reader = new AnnotatedBeanDefinitionReader(this);
        this.scanner = new ClassPathBeanDefinitionScanner(this);
    }

    /**
     * 从其他容器创建容器
     * @param context
     */
    public AnnotationConfigApplicationContext(AnnotationConfigApplicationContext context){
        this();
        this.commonIoc = context.getCommonIoc();
        this.singletonIoc = context.getSingletonIoc();
    }

    /**
     * 传入配置类初始化容器
     */
    public AnnotationConfigApplicationContext(Class<?>... configClass){
        this();
        register(configClass);
        refresh();
    }

    /**
     * 传入待扫描路径初始化容器
     */
    public AnnotationConfigApplicationContext(String... basePackages){
        this();
        scan(basePackages);
        refresh();
    }

    @Override
    public void refresh() {
        // 1. 先处理配置类的bean定义 - 相当于前置处理
        // TODO: 改为前置处理器实现
        doProcessAnnotationConfiguration();
        // 2. 将剩余bean定义实例化
        doInstance();
        // 3. 处理AOP对象
        doAop();
        // 4. 处理依赖注入
        doAutowired();
    }

    /**
     * 预先处理配置类的bean定义
     * Spring中这里使用的是一个BeanDefinitionRegisterPostProcessor来完成的
     */
    private void doProcessAnnotationConfiguration() {

        configClassReader = new ConfigClassReader(this, scanner);

        try {
            for (Map.Entry<String, BeanDefinition> entry : this.beanDefinitionMap.entrySet()) {
                BeanDefinition definition = entry.getValue();
                Class<?> configClazz = Class.forName(definition.getBeanClassName());
                if (configClazz.isAnnotationPresent(Configuration.class)){
                    configClassReader.parseAnnotationConfigClass(configClazz);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 处理Aop切面
     */
    public void doAop(){

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

                        AdvisedSupport myConfig = null;
                        try {
                            // 这里将原有的aopConfig为每个代理类扩增，防止java内存赋值
                            myConfig = getProxyAopConfig(aopConfig, wrappedInstance, wrappedClass);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        if (myConfig.pointCutMatch()){
                            Object proxy = createProxy(myConfig).getProxy();
                            beanWrapper.setWrappedInstance(proxy);  // 将这个二次代理对象包装起来

                            log.debug("为"+ AopUtils.getAopTarget(proxy).getSimpleName() +"创建代理对象 ==> " + proxy.getClass());
                            commonIoc.replace(clazzName, beanWrapper);       // 重新设置commonIoC中的对象为代理对象
                            String beanName = toLowerCase(clazzName.substring(clazzName.lastIndexOf(".") + 1));
                            commonIoc.replace(beanName, beanWrapper);    // 同时更新beanName对应的实例

                            // 如果这个类有接口，同时更新这些接口的实现类对象
                            for (Class<?> anInterface : wrappedClass.getInterfaces()) {
                                String interfaceName = anInterface.getName();
                                String iocBeanInterfaceName = toLowerCase(interfaceName.substring(interfaceName.lastIndexOf(".") + 1));
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
                    if (log.isDebugEnabled()){
                        log.debug("初始化bean对象 : " + beanName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 依赖注入
     */
    private void doAutowired() {
        try {
            for (Map.Entry<String, BeanWrapper> entry : commonIoc.entrySet()) {
                Object instance = entry.getValue().getWrappedInstance();
                // 依赖注入
                populateBean(entry.getKey(), instance);
            }
        }catch (Exception e){
            e.printStackTrace();
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
        return getBean(toLowerCase(beanClass.getSimpleName()));
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
                autowiredBeanName = toLowerCase(type.getSimpleName());
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

        String factoryMethodName = beanDefinition.getFactoryMethodName();
        String beanClassName = beanDefinition.getBeanClassName();

        // 根据factoryMethodName是否为null来区分反射实例和工厂实例
        if (null == factoryMethodName){

            // 这个类拥有beanClassName信息
            // 首先检查是否有beanClassName
            // 使用beanClassName实例化对象 标准反射

            try {
                if (this.singletonIoc.containsKey(beanClassName)){
                    // 如果已经有了该beanDefinition的单例实例缓存，直接获取
                    instance = this.singletonIoc.get(beanClassName);
                } else {

                    // TODO: 这里可以改为使用策略模式
                    Class<?> clazz = Class.forName(beanClassName);
                    instance = clazz.newInstance();
                    // 注解AOP支持
                    if (clazz.isAnnotationPresent(Aspect.class)){
                        // 如果是切面类，加入切面容器
                        aspectBeanInstanceCache.add(clazz);
                    }

                    this.singletonIoc.put(beanClassName, instance); // 加入singletonIoc
                }
            }catch (InstantiationException e){
                throw new Exception("bean实例化错误：" + beanClassName);
            }
        }else{


            // 获取工厂类全类名
            String factoryBeanClassName = beanDefinition.getFactoryBeanName();
            if (null != factoryBeanClassName && !"".equals(factoryBeanClassName.trim())){
                // 这个beanDefinition拥有factoryBeanClassName信息
                // 说明应该是使用工厂方法实例化的（@Bean方式）
                // 因此首先获取工厂bean
                String factoryBeanName = beanDefinition.getFactoryBeanName();

                if (null==factoryBeanName || "".equals(factoryBeanName.trim()) ||
                        null==factoryMethodName || "".equals(factoryMethodName.trim())) {
                    throw new Exception("bean实例化错误：未知factoryBeanName或factoryMethodName");
                }

                // 使用工厂对象创建Bean对象并且加入
                BeanWrapper wrappedFactoryBean = null;
                if (commonIoc.containsKey(factoryBeanClassName)){
                    wrappedFactoryBean = (BeanWrapper) commonIoc.get(factoryBeanClassName);
                }else{
                    // 如果工厂对象还没有被创建
                    wrappedFactoryBean = (BeanWrapper) getBean(factoryBeanName);
                }

                if (null == wrappedFactoryBean){
                    throw new Exception("bean实例化错误：获取对象工厂bean失败");
                }
                Object factoryBeanInUse = wrappedFactoryBean.getWrappedInstance();  // 获取真正的工厂类对象
                Class<?> factoryClazz = Class.forName(factoryBeanClassName);
                Method factoryMethod = factoryClazz.getMethod(factoryMethodName);
                instance = factoryMethod.invoke(factoryBeanInUse);

            }else{
                throw new Exception("bean实例化错误：未知的实例化方式 => " + beanDefinition.getBeanClassName());
            }
        }

        return instance;
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

    public Map<String, Object> getSingletonIoc() {
        return singletonIoc;
    }
    public Map<String, BeanWrapper> getCommonIoc() {
        return commonIoc;
    }

    @Override
    public void register(Class<?>... componentClasses) {
        this.reader.register(componentClasses);
    }

    @Override
    public void scan(String... basePackages) {
        this.scanner.scan(basePackages);
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
        super.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) throws Exception {
        super.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws Exception {
        return super.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return super.beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return StringUtils.toStringArray(super.beanDefinitionMap.keySet());
    }

    @Override
    public int getBeanDefinitionCount() {
        return super.beanDefinitionMap.size();
    }

    @Override
    public boolean isBeanNameInUse(String beanName) {
        return super.beanDefinitionMap.get(beanName) != null;
    }
}
