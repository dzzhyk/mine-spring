package com.yankaizhang.springframework.test.aspect;

import com.yankaizhang.springframework.aop.aopanno.Aspect;
import com.yankaizhang.springframework.aop.aopanno.Before;
import com.yankaizhang.springframework.aop.aopanno.PointCut;
import com.yankaizhang.springframework.aop.aspect.JoinPoint;
import com.yankaizhang.springframework.context.annotation.Component;
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
