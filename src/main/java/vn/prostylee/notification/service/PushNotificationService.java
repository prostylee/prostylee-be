package vn.prostylee.notification.service;

import java.util.concurrent.CompletableFuture;

public interface PushNotificationService<T> {

    CompletableFuture<Boolean> sendPushNotificationAsync(T request);

}
