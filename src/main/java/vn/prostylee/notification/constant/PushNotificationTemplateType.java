package vn.prostylee.notification.constant;

import java.util.Optional;
import java.util.stream.Stream;

public enum PushNotificationTemplateType {
    STORE_NEW_ORDER,
    BUY_AT_STORE;

    public static Optional<PushNotificationTemplateType> find(String type) {
        return Stream.of(PushNotificationTemplateType.values())
                .filter(templateType -> templateType.name().equalsIgnoreCase(type))
                .findFirst();
    }
}
