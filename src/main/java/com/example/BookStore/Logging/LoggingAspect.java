package com.example.BookStore.Logging;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.aspectj.lang.JoinPoint;

import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




@Aspect
@Component
public class LoggingAspect {

    public static final Logger LOGGER=LoggerFactory.getLogger(LoggingAspect.class);




  // Only log controller/service calls (avoid filters and other infrastructure beans)
  @Before("execution(* com.example.BookStore.Controller..*(..)) || execution(* com.example.BookStore.Service..*(..))")
    public void logMethodCall(JoinPoint jp) {
        LOGGER.info("Method Called {}", jp.getSignature().getName());
    }
}