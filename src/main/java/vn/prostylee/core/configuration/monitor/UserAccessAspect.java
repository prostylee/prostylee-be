package vn.prostylee.core.configuration.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.text.MessageFormat.format;

@ConditionalOnProperty(
        value="app.monitor.user-access.enable",
        havingValue = "true",
        matchIfMissing = true)
@Aspect
@Configuration
public class UserAccessAspect {

    private final Logger logger = LogManager.getLogger(getClass());

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.save(..))")
    public void saveMethods() {
    }

    @Pointcut("execution(public * org.springframework.data.repository.Repository+.delete(..))")
    public void deleteMethods() {
    }

    @Before("saveMethods() || deleteMethods()")
    public void before(JoinPoint joinPoint) {
        Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserDetails)
                .map(UserDetails.class::cast)
                .ifPresent(user -> {
                    String username = user.getUsername();
                    String action = joinPoint.getSignature().getName();
                    List<Object> args = Arrays.asList(joinPoint.getArgs());
                    logger.debug(format("User {0} is executing action {1} and args {2}", username, action, args));
                });
    }
}
