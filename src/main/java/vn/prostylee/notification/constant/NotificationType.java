package vn.prostylee.notification.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.stream.Stream;

@AllArgsConstructor
public enum NotificationType {

    // Notify to web user (admin) using firebase
    NEW_ORDER(10, NotificationProvider.FIREBASE),
    JOIN_A_BID(30,  NotificationProvider.FIREBASE),

    // Notify to mobile user (collaborator) using expo push
    CONFIRM_A_BID(31, NotificationProvider.EXPO),
    AUCTION_STARTED(20, NotificationProvider.EXPO),
    ;

    @Getter
    private Integer type;

    @Getter
    private NotificationProvider provider;

    public static Optional<NotificationType> getByType(Integer type) {
        return Stream.of(NotificationType.values()).filter(notificationType -> notificationType.getType().equals(type)).findAny();
    }
}
