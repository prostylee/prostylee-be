package vn.prostylee.core.configuration.monitor.entitycreation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(
        value="app.monitor.create-event.enable",
        havingValue = "true",
        matchIfMissing = true)
@Configuration
@Aspect
@Slf4j
public class EventPublishingAspect {

    @Value("${app.monitor.create-event.entities}")
    private String entities;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @AfterReturning(value = "execution(public * org.springframework.data.repository.Repository+.save*(..))", returning = "entity")
    public void publishEntityCreatedEvent(JoinPoint jp, Object entity) {
        String entityName = entity.getClass().getSimpleName();
        if (isMatchingEntity(entityName)) {
            log.debug("Publish a create event for " + entity);
            eventPublisher.publishEvent(new EntityCreatedEvent(entity));
        }
    }

    private boolean isMatchingEntity(String entityName) {
        return StringUtils.equalsAnyIgnoreCase(entityName, entities.split(","));
    }
}