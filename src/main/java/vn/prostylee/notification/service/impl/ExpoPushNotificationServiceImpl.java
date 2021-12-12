package vn.prostylee.notification.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.request.ExpoPushNotificationRequest;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("expoPushNotificationService")
@Slf4j
public class ExpoPushNotificationServiceImpl implements PushNotificationService<ExpoPushNotificationRequest> {

    @Value("${app.push-notification.url}")
    private String pushNotificationUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public ExpoPushNotificationServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public NotificationProvider getProvider() {
        return NotificationProvider.EXPO;
    }

    @Async
    @Override
    public CompletableFuture<Boolean> sendPushNotificationAsync(ExpoPushNotificationRequest pushNotificationRequest) {
        log.debug("Send push notification " + pushNotificationRequest);
        pushNotificationRequest.setSound("default");
        ResponseEntity<String> response = restTemplate.postForEntity(pushNotificationUrl, pushNotificationRequest, String.class);
        log.debug("Already stored audit successfully with response " + response);

        return CompletableFuture.completedFuture(true);
    }
}
