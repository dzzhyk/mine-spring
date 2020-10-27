package com.yankaizhang.springframework.test.aspect;

import com.yankaizhang.springframework.annotation.Component;
import com.yankaizhang.springframework.annotation.aopanno.Aspect;
import com.yankaizhang.springframework.annotation.aopanno.Before;
import com.yankaizhang.springframework.annotation.aopanno.PointCut;
import com.yankaizhang.springframework.aop.aspect.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @PointCut("public * com.yankaizhang.springframework.test.controller.*.*(*)")
    public void pt(){}


    @Before
    public void before(JoinPoint joinPoint){
        log.info("==> " + joinPoint.getMethod().getName() + "执行了");
    }

}
