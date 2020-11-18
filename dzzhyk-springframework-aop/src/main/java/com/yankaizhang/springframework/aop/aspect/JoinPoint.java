package com.yankaizhang.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切点抽象，包含切入方法的所有信息
 * @author dzzhyk
 */
public interface JoinPoint {

    /**
     * 获取切点方法
     * @return 切点方法对象
     */
    Method getMethod();

    /**
     * 获取切点参数
     * @return 切点方法参数列表
     */
    Object[] getArguments();

    /**
     * 获取切点对象
     * @return 切入点对象
     */
    Object getTarget();

}
