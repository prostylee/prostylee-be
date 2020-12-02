package vn.prostylee.core.configuration.logging;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@ConditionalOnProperty(
        value="app.monitor.performance.enable",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
@Aspect
@Slf4j
public class MethodExecutionTimeLoggingAdvice {

    private static final long MAX_TIME_EXECUTION_IN_MS = 2000;

    @Pointcut("execution(public * vn.prostylee..*.controller..*.*(..))")
    public void controllerLayer() { }

    @Pointcut("execution(public * vn.prostylee..*.service..*.*(..))")
    public void serviceLayer() { }

    @Pointcut("execution(public * vn.prostylee..*.repository..*.*(..))")
    public void repositoryLayer() { }

    @Around("controllerLayer() || serviceLayer() || repositoryLayer()")
    public Object logExecutions(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getCanonicalName();
        String methodName = methodSignature.getName();

        log.info("Entering... className={}, methodName={}, startTime={}", className, methodName, LocalDateTime.now());

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            return joinPoint.proceed();
        } finally {
            stopWatch.stop();
            if (stopWatch.getTime() > MAX_TIME_EXECUTION_IN_MS) {
                log.warn("Leaving... className={}, methodName={}, endTime={}", className, methodName, LocalDateTime.now());
            } else {
                log.info("Leaving... className={}, methodName={}, endTime={}", className, methodName, LocalDateTime.now());
            }
        }

    }

}