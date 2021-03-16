package com.yankaizhang.spring.example.aspect;

import com.yankaizhang.spring.aop.annotation.*;
import com.yankaizhang.spring.aop.aspect.JoinPoint;
import com.yankaizhang.spring.context.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class MyAspect2 {

    private static final Logger log = LoggerFactory.getLogger(MyAspect2.class);

    @PointCut("com.yankaizhang.spring.example.controller.*.*(*)")
    public void pt(){}

    @Before
    public void before(JoinPoint joinPoint){
        log.info("执行 {} 方法 => 前置处理 => MyAspect2", joinPoint.getMethod().getName());
    }

    @AfterReturning
    public void after(JoinPoint joinPoint){
        log.info("执行 {} 方法 => 后置处理 => MyAspect2", joinPoint.getMethod().getName());
    }

    @AfterThrowing
    public void error(JoinPoint joinPoint, Throwable throwable){
        log.info("执行 {} 方法 => 异常处理，异常原因 => {}  => MyAspect2", joinPoint.getMethod().getName(), throwable.getMessage());
        throwable.printStackTrace();
    }
}
