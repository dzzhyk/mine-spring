package test.aspect;

import com.yankaizhang.springframework.aop.aspect.JoinPoint;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义切点类
 */
@Slf4j
public class LogAspect {

    /**
     * 前置通知
     */
    public void before(JoinPoint joinPoint){
        log.info(joinPoint.getMethod().getName() + "开始...");
    }

    /**
     * 后置通知
     */
    public void after(JoinPoint joinPoint){

        log.info(joinPoint.getMethod().getName() + "结束...");
    }

    /**
     * 异常通知
     */
    public void afterThrowing(JoinPoint joinPoint, Throwable ex){
        log.info(joinPoint.getMethod().getName() + "异常...");
        log.warn("异常原因：" + ex.getMessage());
    }

}
