package com.yankaizhang.spring.example.aspect;

import com.yankaizhang.spring.aop.annotation.*;
import com.yankaizhang.spring.aop.aspect.JoinPoint;
import com.yankaizhang.spring.context.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class MyAspect {

    private static final Logger log = LoggerFactory.getLogger(MyAspect.class);

    @PointCut("com.yankaizhang.spring.example.controller.TestController.*(*)")
    public void pt(){}

    @Before
    public void before(JoinPoint joinPoint){
        log.info("执行 {} 方法 => 前置处理，方法入参 : {}", joinPoint.getMethod().getName(), joinPoint.getArguments());
    }

    @AfterReturning
    public void after(JoinPoint joinPoint, Object result){
        log.info("执行 {} 方法 => 后置处理, 方法出参 : {}", joinPoint.getMethod().getName(), result);
    }

    @AfterThrowing
    public void error(JoinPoint joinPoint, Throwable throwable){
        log.info("执行 {} 方法 => 异常处理，异常原因 => {}", joinPoint.getMethod().getName(), throwable.getMessage());
        throwable.printStackTrace();
    }
}
