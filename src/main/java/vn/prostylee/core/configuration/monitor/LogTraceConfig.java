package vn.prostylee.core.configuration.monitor;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        value="app.monitor.trace.enable",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
@Aspect
public class LogTraceConfig {

    /**
     * Change the setting: logging.level.root=TRACE to enable this feature
     * @see {https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/aop/interceptor/CustomizableTraceInterceptor.html}
     */
    @Bean
    public CustomizableTraceInterceptor interceptor() {
        CustomizableTraceInterceptor interceptor = new CustomizableTraceInterceptor();
        interceptor.setEnterMessage("Entering $[methodName]($[arguments]).");
        interceptor.setExitMessage("Leaving $[methodName](..) with return value $[returnValue], took $[invocationTime]ms.");
        return interceptor;
    }

    @Bean
    public Advisor jpaRepositoryAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public * org.springframework.data.repository.Repository+.*(..))");
        return new DefaultPointcutAdvisor(pointcut, interceptor());
    }
}