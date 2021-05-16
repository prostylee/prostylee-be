package vn.prostylee.core.configuration.monitor.entitycreation;

import org.springframework.context.ApplicationEvent;

public class EntityCreatedEvent extends ApplicationEvent {

    public EntityCreatedEvent(Object source) {
        super(source);
    }
}