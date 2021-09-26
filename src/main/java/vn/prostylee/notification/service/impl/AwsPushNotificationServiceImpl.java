package vn.prostylee.notification.service.impl;

import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.*;
import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.notification.configuration.AwsPinpointProperties;
import vn.prostylee.notification.dto.request.AwsPushNotificationRequest;
import vn.prostylee.notification.service.PushNotificationService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("awsPushNotificationService")
@Slf4j
@RequiredArgsConstructor
public class AwsPushNotificationServiceImpl implements PushNotificationService<AwsPushNotificationRequest> {

    private final AwsPinpointProperties awsPinpointProperties;

    @Override
    public CompletableFuture<Boolean> sendPushNotificationAsync(AwsPushNotificationRequest request) {

        if (CollectionUtils.isEmpty(request.getTokens())) {
            return CompletableFuture.completedFuture(false);
        }

        sendGCMToDevice(request);
        return CompletableFuture.completedFuture(true);
    }

    private boolean sendGCMToDevice(AwsPushNotificationRequest request) {
        int numberOfSuccessNotifications = ChunkServiceExecutor.execute(request.getTokens(), tokens -> {
            tokens.forEach(token -> {
                try {
                    log.debug("Send push notification " + request);

                    MessageRequest messageRequest = buildMessageRequest(token);

                    SendMessagesRequest sendMessagesRequest = new SendMessagesRequest()
                            .withApplicationId("")
                            .withMessageRequest(messageRequest);

                    AmazonPinpoint client = AmazonPinpointClientBuilder.standard()
                            .withRegion(awsPinpointProperties.getRegion()).build();

                    client.sendMessages(sendMessagesRequest);
                    ResponseEntity<String> response = null;
                    log.debug("Already stored audit successfully with response " + response);
                } catch (Exception e) {
                    log.error("Fail to send firebase notification to device ", e);
                }
            });
            return tokens.size();
        });
        return numberOfSuccessNotifications > 0;
    }

    private MessageRequest buildMessageRequest(String token) {
        Map<String, String> data = new HashMap<>();

        DirectMessageConfiguration directMessageConfiguration =
                new DirectMessageConfiguration()
                        .withGCMMessage(new GCMMessage().withData(data).withSilentPush(true));

        AddressConfiguration addressConfiguration = new AddressConfiguration().withChannelType(ChannelType.GCM);

        return new MessageRequest().withMessageConfiguration(directMessageConfiguration)
                .addAddressesEntry(token, addressConfiguration);
    }
}
