package com.first.first_app.Aspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.first.first_app.Service..*(..))")
    public void serviceLayerPointcut() {}

    @Around("serviceLayerPointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        
        long startTime = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        String params = Arrays.toString(args);

        logger.info("Calling: {} with args: {}", methodName, params);
        
        try {
            Object result = joinPoint.proceed();
            
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            logger.info("Returned from: {} with result: {} in {}ms", 
                        methodName, 
                        result, 
                        duration);
            
            return result;

        } catch (Throwable ex) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            logger.error("Exception in: {} - {} after {}ms. Full trace: {}", 
                         methodName, 
                         ex.getMessage(), 
                         duration,
                         ex);
            
            throw ex; 
        }
    }
}