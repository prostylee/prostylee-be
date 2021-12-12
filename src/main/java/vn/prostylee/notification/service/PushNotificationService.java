package vn.prostylee.notification.service;

import vn.prostylee.notification.constant.NotificationProvider;

import java.util.concurrent.CompletableFuture;

public interface PushNotificationService<T> {

    NotificationProvider getProvider();

    CompletableFuture<Boolean> sendPushNotificationAsync(T request);

}
