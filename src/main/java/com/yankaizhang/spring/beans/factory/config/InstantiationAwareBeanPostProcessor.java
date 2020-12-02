package com.yankaizhang.spring.beans.factory.config;

import com.yankaizhang.spring.beans.PropertyValues;

/**
 * {@link BeanPostProcessor}的一个子接口，提供了在bean实例化前后的处理
 * Instantiation	表示实例化，对象还未生成
 * @author dzzhyk
 * @since 2020-12-02 15:09:40
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在bean实例化之前执行的方法
     * 我们可以返回任何类型的值。
     * 由于这个时候目标对象还未实例化，所以这个返回值可以用来代替原本该生成的目标对象的实例(比如代理对象)。
     * 如果该方法的返回值代替原本该生成的目标对象，后续只有postProcessAfterInitialization方法会调用，其它方法不再调用；
     * 否则按照正常的流程走
     *
     * @param beanClass bean实例Class
     * @param beanName bean名称
     * @return 处理后对象
     * @throws Exception 处理过程异常
     */
    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws Exception{
        return null;
    }

    /**
     * 在bean实例化之后执行的方法
     * 在目标对象实例化之后调用，这个时候对象已经被实例化，但是该实例的属性还未被设置，都是null。
     * 该方法返回值用于决定要不要调用postProcessProperties方法
     *
     * @param bean 待处理bean对象
     * @param beanName bean名称
     * @return 如果返回false，则postProcessProperties被忽略不执行；如果返回true，postProcessProperties会执行
     * @throws Exception 处理过程异常
     */
    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws Exception{
        return true;
    }

    /**
     * 在实例化之后填充bean的属性，如果postProcessAfterInstantiation方法返回false，该方法可能不会被调用
     * @param pvs 属性值
     * @param bean 实例化后的bean对象
     * @param beanName bean名称
     * @return bean实例被填充进去的属性值
     * @throws Exception 处理过程异常
     */
    default PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws Exception {
        return null;
    }
}
