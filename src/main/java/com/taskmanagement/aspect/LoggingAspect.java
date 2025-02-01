package com.taskmanagement.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.Objects;


@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoggingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.taskmanagement.controller..*(..))")
    public void provideValuesForLogging() {}

    @AfterReturning(pointcut = "provideValuesForLogging()", returning = "returnValue")
    public void logMethods(JoinPoint joinPoint, Object returnValue) {

        try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String host = request.getHeader("Host");
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            String requestBody = null;
            String responseBody = returnValue.toString();

            doLog(joinPoint, request, host, ipAddress, requestBody, responseBody);

        } catch (Exception e) {
            logger.info("AN EXCEPTION OCCURRED DUE TO :::: {0} ", e);
        }
    }

    @AfterThrowing(pointcut = "provideValuesForLogging()", throwing = "ex")
    public void logExceptions(JoinPoint joinPoint, Throwable ex) {

        try {

            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            String host = request.getHeader("Host");
            String ipAddress = request.getHeader("X-FORWARDED-FOR");
            String requestBody = null;
            String responseBody =
                    ex.getLocalizedMessage() + "||" + ex.getMessage() + "||" + ex.getCause();

            doLog(joinPoint, request, host, ipAddress, requestBody, responseBody);

        } catch (Exception e) {
            int lineNumber = e.getStackTrace()[0].getLineNumber();
            logger.info("Line number {}", lineNumber);
            logger.info("AN EXCEPTION OCCURRED DUE TO :::: {0}", e);
        }
    }

    private void doLog(
            JoinPoint joinPoint,
            HttpServletRequest request,
            String host,
            String ipAddress,
            String requestBody,
            String responseBody)
            throws JsonProcessingException {
        if (joinPoint.getArgs().length > 0) {
            Object requestObj = joinPoint.getArgs()[0];
            if (!Objects.equals(request.getMethod(), "GET")) {
                requestBody = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestObj);
            }
        }
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        String logMessage = String.format(
                "Request URL: %s%n" +
                        "Request Method: %s%n" +
                        "Request Headers: %s%n" +
                        "Request Body: %s%n" +
                        "IPAddress for request: %s%n" +
                        "Response Body: %s%n" +
                        "Host: %s%n" +
                        "Method name: %s",
                request.getRequestURL(),
                request.getMethod(),
                Collections.list(request.getHeaderNames()),
                requestBody,
                ipAddress,
                responseBody,
                host,
                joinPoint.getSignature().getName()
        );

        logger.info(logMessage);
    }
}
