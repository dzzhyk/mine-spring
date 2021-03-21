package com.aspect;

import com.yankaizhang.spring.aop.annotation.AfterReturning;
import com.yankaizhang.spring.aop.annotation.Aspect;
import com.yankaizhang.spring.aop.annotation.Before;
import com.yankaizhang.spring.aop.annotation.PointCut;
import com.yankaizhang.spring.aop.aspect.JoinPoint;
import com.yankaizhang.spring.context.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Aspect
@Component
public class MyAspect {

    private static final Logger log = LoggerFactory.getLogger(MyAspect.class);

    @PointCut("com.component.CompA.*(*)")
    public void pt(){}

    @Before
    public void before(JoinPoint joinPoint){
        log.info("前置通知 => {}", joinPoint.getMethod().getName());
    }

    @AfterReturning
    public void after(JoinPoint joinPoint){
        log.info("后置通知 => {}", joinPoint.getMethod().getName());
    }
}
