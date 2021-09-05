package vn.prostylee.notification.constant;

import java.util.Optional;
import java.util.stream.Stream;

public enum NotificationProvider {
    EXPO, FIREBASE, AWS_PINPOINT;

    public static Optional<NotificationProvider> findProvider(String provider) {
        return Stream.of(NotificationProvider.values())
                .filter(notificationProvider -> notificationProvider.name().equalsIgnoreCase(provider))
                .findAny();
    }
}
