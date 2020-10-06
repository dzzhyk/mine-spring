package com.yankaizhang.springframework.aop.aspect;

import java.lang.reflect.Method;

/**
 * 切点抽象，包含切入方法的所有信息
 */
public interface JoinPoint {

    Method getMethod();

    Object[] getArguments();

    Object getTarget();

}
