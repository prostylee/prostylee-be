package vn.prostylee.notification.event.push;

import org.springframework.context.ApplicationEvent;

public class PushNotificationEvent extends ApplicationEvent {


    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param pushNotificationEventDto the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public PushNotificationEvent(PushNotificationEventDto<?> pushNotificationEventDto) {
        super(pushNotificationEventDto);
    }
}
