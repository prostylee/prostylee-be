package vn.prostylee.notification.service;

import vn.prostylee.notification.dto.request.FcmPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmSubscriptionRequest;

public interface FcmPushNotificationService {

    boolean sendPnsToDevice(FcmPushNotificationRequest notificationRequestDto);

    void subscribeToTopic(FcmSubscriptionRequest subscriptionRequestDto);

    void unsubscribeFromTopic(FcmSubscriptionRequest subscriptionRequestDto);

    boolean sendPnsToTopic(FcmSubscriptionRequest notificationRequestDto);
}
