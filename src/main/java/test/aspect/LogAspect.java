package test.aspect;

import com.yankaizhang.springframework.annotation.Component;
import com.yankaizhang.springframework.annotation.aopanno.*;
import com.yankaizhang.springframework.aop.aspect.JoinPoint;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义切点类
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    @PointCut("public * test.service.impl.*.*(*)")
    public void pt(){

    }

    /**
     * 前置通知
     */
    @Before("pt()")
    public void before(JoinPoint joinPoint){
        log.info("切面1 " + joinPoint.getMethod().getName() + "开始...");
    }

    /**
     * 后置通知
     */
    @AfterReturning("pt()")
    public void after(JoinPoint joinPoint){

        log.info("切面1 " + joinPoint.getMethod().getName() + "结束...");
    }

    /**
     * 异常通知
     */
    @AfterThrowing("pt()")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        log.info(joinPoint.getMethod().getName() + "异常...");
        log.warn("异常原因：" + ex.getMessage());
    }

}
