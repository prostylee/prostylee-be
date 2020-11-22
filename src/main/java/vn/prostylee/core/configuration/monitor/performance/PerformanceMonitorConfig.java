package vn.prostylee.core.configuration.monitor.performance;

import vn.prostylee.core.constant.AppConstant;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        value="app.monitor.performance.enable",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
@Aspect
@Slf4j
public class PerformanceMonitorConfig {

    /**
     * Pointcut for execution of methods on classes annotated with {@link org.springframework.stereotype.Service}
     * annotation
     */
    @Pointcut("execution(public * (@org.springframework.stereotype.Service " + AppConstant.BASE_PACKAGE + "..*).*(..))")
    public void serviceAnnotation() { }

    /**
     * Pointcut for execution of methods on classes annotated with
     * {@link org.springframework.stereotype.Repository} annotation
     */
    @Pointcut("execution(public * (@org.springframework.stereotype.Repository " + AppConstant.BASE_PACKAGE + "..*).*(..))")
    public void repositoryAnnotation() { }

    @Pointcut("serviceAnnotation() || repositoryAnnotation()")
    public void performanceMonitor() { }

    /**
     * Simple AOP Alliance MethodInterceptor for performance monitoring.
     * This interceptor has no effect on the intercepted method call.
     */
    @Bean
    public CustomPerformanceMonitorInterceptor customPerformanceMonitorInterceptor() {
        return new CustomPerformanceMonitorInterceptor(true);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(PerformanceMonitorConfig.class.getCanonicalName() + ".performanceMonitor()");
        return new DefaultPointcutAdvisor(pointcut, customPerformanceMonitorInterceptor());
    }
}