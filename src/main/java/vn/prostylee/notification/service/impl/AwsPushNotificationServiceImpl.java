package vn.prostylee.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.pinpoint.PinpointClient;
import software.amazon.awssdk.services.pinpoint.model.*;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.notification.configuration.AwsPinpointProperties;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.request.AwsPushNotificationRequest;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@Qualifier("awsPushNotificationService")
@Slf4j
@RequiredArgsConstructor
public class AwsPushNotificationServiceImpl implements PushNotificationService<AwsPushNotificationRequest> {

    private final AwsPinpointProperties awsPinpointProperties;
    private final PinpointClient pinpointClient;

    @Override
    public NotificationProvider getProvider() {
        return NotificationProvider.AWS_PINPOINT;
    }

    @Override
    public CompletableFuture<Boolean> sendPushNotificationAsync(AwsPushNotificationRequest request) {

        if (CollectionUtils.isEmpty(request.getTo())) {
            return CompletableFuture.completedFuture(false);
        }

        sendGCMToDevice(request);
        return CompletableFuture.completedFuture(true);
    }

    private boolean sendGCMToDevice(AwsPushNotificationRequest request) {
        int numberOfSuccessNotifications = ChunkServiceExecutor.execute(request.getTo(), tokens -> {
            tokens.forEach(token -> {
                try {
                    log.debug("Send push notification " + request);

                    AddressConfiguration addConfig = AddressConfiguration.builder()
                            .channelType(ChannelType.GCM)
                            .build();

                    MessageRequest messageRequest = buildMessageRequest(token, request);

                    SendMessagesRequest sendMessagesRequest = SendMessagesRequest.builder()
                            .applicationId(awsPinpointProperties.getAppId())
                            .messageRequest(messageRequest)
                            .build();

                    SendMessagesResponse result = pinpointClient.sendMessages(sendMessagesRequest);
                    log.debug("Already send notification successfully with result: " + result);
                } catch (Exception e) {
                    log.error("Fail to send firebase notification to device ", e);
                }
            });
            return tokens.size();
        });
        return numberOfSuccessNotifications > 0;
    }

    private MessageRequest buildMessageRequest(String token, AwsPushNotificationRequest request) {
        Map<String, String> data = Optional.ofNullable(request.getData())
                .orElse(new HashMap<>())
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue())));

        GCMMessage message = GCMMessage.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .data(data)
                .url(request.getLink())
                .silentPush(request.getSilentPush())
                .build();

        log.debug("message={}", message);

        DirectMessageConfiguration directMessageConfiguration = DirectMessageConfiguration.builder()
                .gcmMessage(message)
                .build();

        AddressConfiguration addressConfiguration = AddressConfiguration.builder()
                .channelType(ChannelType.GCM)
                .build();

        Map<String, AddressConfiguration> addressMap = new HashMap<>();
        addressMap.put(token, addressConfiguration);
        return MessageRequest.builder()
                .messageConfiguration(directMessageConfiguration)
                .addresses(addressMap)
                .build();
    }
}
