package vn.prostylee.notification.service.impl;

import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.request.FcmPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmSubscriptionRequest;
import vn.prostylee.notification.service.FcmPushNotificationService;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
@Qualifier("fcmPushNotificationService")
public class FcmPushNotificationServiceImpl implements FcmPushNotificationService, PushNotificationService<FcmPushNotificationRequest> {

    private final FirebaseMessaging firebaseMessaging;

    @Override
    public boolean sendPnsToDevice(FcmPushNotificationRequest notificationRequestDto) {
        if (CollectionUtils.isEmpty(notificationRequestDto.getTo())) {
            return false;
        }

        final Notification notification = buildNotification(notificationRequestDto);
        final WebpushConfig webpushConfig = buildWebpushConfig(notificationRequestDto);
        final String jsonContent = JsonUtils.toJson(notificationRequestDto.getData());

        int numberOfNotifications = ChunkServiceExecutor.execute(notificationRequestDto.getTo(), tokens -> {
            tokens.forEach(token -> {
                try {
                    Message message = Message.builder()
                            .setToken(token)
                            .setNotification(notification)
                            .putData("content", jsonContent)
                            .setWebpushConfig(webpushConfig)
                            .build();
                    FirebaseMessaging.getInstance().send(message);
                } catch (FirebaseMessagingException e) {
                    log.error("Fail to send firebase notification to device ", e);
                }
            });
            return tokens.size();
        });
        return numberOfNotifications > 0;
    }

    private Notification buildNotification(FcmPushNotificationRequest notificationRequestDto) {
        return Notification.builder()
                .setTitle(notificationRequestDto.getTitle())
                .setBody(notificationRequestDto.getBody())
                .build();
    }

    private WebpushConfig buildWebpushConfig(FcmPushNotificationRequest notificationRequestDto) {
        WebpushConfig webpushConfig = null;
        if (StringUtils.isNotBlank(notificationRequestDto.getLink())) {
            WebpushFcmOptions fcmOptions = WebpushFcmOptions.builder()
                    .setLink(notificationRequestDto.getLink())
                    .build();

            webpushConfig = WebpushConfig.builder().setFcmOptions(fcmOptions).build();
        }
        return webpushConfig;
    }

    @Override
    public void subscribeToTopic(FcmSubscriptionRequest subscriptionRequestDto) {
        if (CollectionUtils.isEmpty(subscriptionRequestDto.getTo())) {
            return;
        }
        try {
            firebaseMessaging.subscribeToTopic(subscriptionRequestDto.getTo(), subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase subscribe to topic fail: " + subscriptionRequestDto.getTopicName(), e);
        }
    }

    @Override
    public void unsubscribeFromTopic(FcmSubscriptionRequest subscriptionRequestDto) {
        if (CollectionUtils.isEmpty(subscriptionRequestDto.getTo())) {
            return;
        }
        try {
            firebaseMessaging.unsubscribeFromTopic(subscriptionRequestDto.getTo(), subscriptionRequestDto.getTopicName());
        } catch (FirebaseMessagingException e) {
            log.error("Firebase unsubscribe from topic fail: " + subscriptionRequestDto.getTopicName(), e);
        }
    }

    @Override
    public boolean sendPnsToTopic(FcmSubscriptionRequest subscriptionRequestDto) {
        if (CollectionUtils.isEmpty(subscriptionRequestDto.getTo())) {
            return false;
        }

        subscribeToTopic(subscriptionRequestDto);

        final Notification notification = buildNotification(subscriptionRequestDto);
        final WebpushConfig webpushConfig = buildWebpushConfig(subscriptionRequestDto);
        final String jsonContent = JsonUtils.toJson(subscriptionRequestDto.getData());

        int numberOfNotifications = ChunkServiceExecutor.execute(subscriptionRequestDto.getTo(), tokens -> {
            tokens.forEach(token -> {
                try {
                    Message message = Message.builder()
                            .setTopic(subscriptionRequestDto.getTopicName())
                            .setNotification(notification)
                            .putData("content", jsonContent)
                            .setWebpushConfig(webpushConfig)
                            .build();
                    FirebaseMessaging.getInstance().send(message);
                } catch (FirebaseMessagingException e) {
                    log.error("Fail to send firebase subscription to topic " + subscriptionRequestDto.getTopicName(), e);
                }
            });
            return tokens.size();
        });
        return numberOfNotifications > 0;
    }

    @Override
    public NotificationProvider getProvider() {
        return NotificationProvider.FIREBASE;
    }

    @Async
    @Override
    public CompletableFuture<Boolean> sendPushNotificationAsync(FcmPushNotificationRequest request) {
        if (CollectionUtils.isEmpty(request.getTo())) {
            return CompletableFuture.completedFuture(false);
        }

        if (request instanceof FcmSubscriptionRequest) {
            sendPnsToTopic((FcmSubscriptionRequest) request);
        } else {
            sendPnsToDevice(request);
        }

        return CompletableFuture.completedFuture(true);
    }
}
