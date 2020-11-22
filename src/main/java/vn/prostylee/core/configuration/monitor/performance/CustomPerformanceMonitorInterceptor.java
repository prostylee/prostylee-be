package vn.prostylee.core.configuration.monitor.performance;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.AbstractMonitoringInterceptor;

import java.time.LocalDateTime;

/**
 * Simple AOP Alliance MethodInterceptor for performance monitoring.
 * This interceptor has no effect on the intercepted method call.
 */
@Slf4j
public class CustomPerformanceMonitorInterceptor extends AbstractMonitoringInterceptor {

    public CustomPerformanceMonitorInterceptor() {
        super();
    }

    public CustomPerformanceMonitorInterceptor(boolean useDynamicLogger) {
        setUseDynamicLogger(useDynamicLogger);
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
            throws Throwable {
        String name = createInvocationTraceName(invocation);
        long start = System.currentTimeMillis();
        log.info("Method " + name + " execution started at:" + LocalDateTime.now());
        try {
            return invocation.proceed();
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;

            log.info("Method " + name + " execution lasted:" + time + " ms");
            log.info("Method " + name + " execution ended at:" + LocalDateTime.now());

            if (time > 3000) {
                log.warn("Method execution longer than 3 seconds!");
            }
        }
    }
}