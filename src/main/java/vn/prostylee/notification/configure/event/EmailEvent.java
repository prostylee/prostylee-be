package vn.prostylee.notification.configure.event;

import org.springframework.context.ApplicationEvent;

public class EmailEvent extends ApplicationEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param emailEventDto the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public EmailEvent(EmailEventDto<?> emailEventDto) {
        super(emailEventDto);
    }
}
