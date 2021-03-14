package com.yankaizhang.spring.aop.support;

import com.yankaizhang.spring.aop.AopProxy;
import com.yankaizhang.spring.aop.annotation.Aspect;
import com.yankaizhang.spring.aop.annotation.PointCut;
import com.yankaizhang.spring.aop.impl.CglibAopProxy;
import com.yankaizhang.spring.aop.impl.JdkDynamicAopProxy;
import com.yankaizhang.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.yankaizhang.spring.beans.holder.BeanWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 代理类bean对象处理器，用于在切面对象bean初始化的时候执行处理
 * @author dzzhyk
 * @since 2021-03-14 12:36:40
 */
public class AutoProxyCreator implements InstantiationAwareBeanPostProcessor {

    private static final Logger log = LoggerFactory.getLogger(AutoProxyCreator.class);

    private AopAnnotationReader reader = new AopAnnotationReader();
    private List<AdvisedSupport> advisedSupports = null;
    private boolean aspectParsed = false;

    /**
     * 所有切面类被扫描到之后，执行这个方法
     */
    public void parseAspect(){
        advisedSupports = reader.parseAspect();
        if (advisedSupports != null){
            // 保证所有切面按照切点表达式顺序执行
            advisedSupports.sort(new AdviceSupportComparator());
        }
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws RuntimeException {
        // 收集切面类信息
        if (beanClass.isAnnotationPresent(Aspect.class)){
            postProcessAdvice(beanClass);
        }
        return null;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws RuntimeException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws RuntimeException {

        if (!aspectParsed){
            parseAspect();
            aspectParsed = true;
        }

        if (advisedSupports == null || advisedSupports.size() <= 0 || bean == null){
            return bean;
        }

        // 如果有代理类需要执行
        for (AdvisedSupport aopConfig : advisedSupports) {

            Object wrappedInstance = null;
            Class<?> wrappedClass = null;
            if (bean instanceof BeanWrapper) {
                wrappedInstance = ((BeanWrapper) bean).getWrappedInstance();
                wrappedClass = ((BeanWrapper) bean).getWrappedClass();
            }else{
                wrappedInstance = new BeanWrapper(bean);
                wrappedClass = ((BeanWrapper) wrappedInstance).getWrappedClass();
            }

            AdvisedSupport myConfig = null;

            try {
                // 这里将原有的aopConfig为每个代理类扩增，防止java内存赋值
                myConfig = getProxyAopConfig(aopConfig, wrappedInstance, wrappedClass);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (myConfig.pointCutMatch()) {
                Object proxy = createProxy(myConfig).getProxy();
                bean = new BeanWrapper(proxy);
                log.debug("为" + AopUtils.getAopTarget(proxy).getSimpleName() + "创建代理对象 : " + proxy.getClass());
            }

        }
        return bean;
    }


    /**
     * 收集当前切面类定义的切点表达式expression
     * @param aspectClazz 当前切面类对象
     */
    private void postProcessAdvice(Class<?> aspectClazz){
        Method[] aspectClazzMethods = aspectClazz.getDeclaredMethods();

        // 加载切点类PointCut
        List<Method> aspectMethods = new ArrayList<>(aspectClazzMethods.length);
        Collections.addAll(aspectMethods, aspectClazzMethods);

        // 首先处理所有PointCut，获取所有已知的当类切点表达式
        for (Method method : aspectMethods) {
            if (method.isAnnotationPresent(PointCut.class)) {
                // 如果定义了切点类，保存切点表达式，一个切面类
                String execution = method.getAnnotation(PointCut.class).value().trim();
                reader.setPointCut(aspectClazz, execution);
                break;
            }
        }
    }

    /**
     * 创建代理对象
     * @param config 一个AopConfig包装类
     */
    private AopProxy createProxy(AdvisedSupport config) {
        if (config.getTargetClass().getInterfaces().length > 0) {
            // 如果对象存在接口，默认使用jdk动态代理
            return new JdkDynamicAopProxy(config);
        }
        // 如果对象没有接口，使用CGLib动态代理
        return new CglibAopProxy(config);
    }


    /**
     * 根据已有的aopConfig得到新的aopConfig实例，同时设置TargetClass
     */
    private AdvisedSupport getProxyAopConfig(AdvisedSupport advisedSupport, Object wrappedInstance, Class<?> wrappedClass)
            throws Exception {
        AdvisedSupport myAdviceSupport = new AdvisedSupport(advisedSupport.getAopConfig());
        myAdviceSupport.setTarget(wrappedInstance);
        myAdviceSupport.setTargetClass(wrappedClass);
        return myAdviceSupport;
    }
}
