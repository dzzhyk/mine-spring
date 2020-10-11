package test.aspect;

import com.yankaizhang.springframework.annotation.Component;
import com.yankaizhang.springframework.annotation.aopanno.*;
import com.yankaizhang.springframework.aop.aspect.JoinPoint;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect2 {


    @PointCut("test.service.impl.UserServiceImpl.*(*)")
    public void pt2(){

    }

    @Before
    public void before(JoinPoint joinPoint){
        log.info("切面2 " + joinPoint.getMethod().getName() + "开始执行...");
    }

    @AfterReturning
    public void after(JoinPoint joinPoint){
        log.info("切面2 " + joinPoint.getMethod().getName() + "执行结束...");
    }

    @AfterThrowing
    public void afterThrow(JoinPoint joinPoint, Throwable ex){
        log.info(joinPoint.getMethod().getName() + "异常...");
        log.warn("异常原因：" + ex.getMessage());
    }

}
