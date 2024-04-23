package com.gypApp_workLoadService.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class RestCallLoggingAspect {


    @Pointcut("execution(* com.gypApp_workLoadService.controller.*.*(..))")
    public void restControllerMethods() {
    }

    @Before("restControllerMethods()")
    public void logRestCallDetails(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.info("REST Call: {} [{}]", request.getRequestURI(), request.getMethod());
        log.info("Class: {}, Method: {}", className, methodName);
        log.info("Request Parameters: {}", extractRequestParameters(request));
    }

    @AfterReturning(pointcut = "restControllerMethods()", returning = "result")
    public void logRestCallResult(Object result) {
        log.info("REST Call Result: {}", extractResponseDetails(result));
    }

    public String extractRequestParameters(HttpServletRequest request) {
        StringBuilder parameters = new StringBuilder();
        request.getParameterMap().forEach((key, values) ->
                parameters.append(key).append("=").append(String.join(",", values)).append("; "));
        return parameters.toString();
    }

    public String extractResponseDetails(Object result) {
        if (result instanceof org.springframework.http.ResponseEntity) {
            org.springframework.http.ResponseEntity<?> responseEntity = (org.springframework.http.ResponseEntity<?>) result;
            return "Status: " + responseEntity.getStatusCodeValue() + ", Response Body: " + responseEntity.getBody();
        } else {
            return "Invalid response type";
        }
    }
}