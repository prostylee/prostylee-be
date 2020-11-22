package vn.prostylee.core.configuration.monitor.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EntityCreationEventListener implements ApplicationListener<EntityCreatedEvent> {

    @Async
    @Override
    public void onApplicationEvent(EntityCreatedEvent event) {
        log.info("Created instance: " + event.getSource().toString());
    }
}