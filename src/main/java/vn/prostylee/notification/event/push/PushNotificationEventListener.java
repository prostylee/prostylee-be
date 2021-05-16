package vn.prostylee.notification.event.push;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.core.provider.ThymeleafTemplateProcessor;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.response.PushNotificationTemplateResponse;
import vn.prostylee.notification.service.NotificationService;
import vn.prostylee.notification.service.PushNotificationTemplateService;
import vn.prostylee.notification.service.PushNotificationTokenService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationEventListener implements ApplicationListener<PushNotificationEvent> {

    private final PushNotificationTemplateService templateService;
    private final PushNotificationTokenService tokenService;
    private final NotificationService notificationService;
    private final ThymeleafTemplateProcessor templateProcessor;

    @Async
    @Override
    public void onApplicationEvent(PushNotificationEvent event) {
        log.info("Created instance: event={}", event.getSource());
        if (!(event.getSource() instanceof PushNotificationEventDto)) {
            log.warn("Can not handle push notification event with source type is {}", event.getSource());
            return;
        }

        try {
            PushNotificationEventDto<?> eventData = (PushNotificationEventDto<?>) event.getSource();
            PushNotificationTemplateResponse template = templateService.findByType(eventData.getTemplateType().name());

            Set<UserToken> tokens = new HashSet<>();
            if (CollectionUtils.isNotEmpty(eventData.getUserIds())) {
                tokens.addAll(tokenService.getTokensByUserIds(eventData.getUserIds()));
            }

            if (CollectionUtils.isNotEmpty(eventData.getStoreIds())) {
                tokens.addAll(tokenService.getTokensByStoreIds(eventData.getStoreIds()));
            }

            if (tokens.isEmpty()) {
                log.warn("There is no device to receive a push notification with eventData={}", eventData);
                return;
            }

            PushNotificationDto pushNotificationRequest = PushNotificationDto.builder()
                    .title(templateProcessor.process(template.getTitle(), eventData.getFillData()))
                    .body(templateProcessor.process(template.getContent(), eventData.getFillData()))
                    .provider(StringUtils.defaultIfBlank(eventData.getProvider(), NotificationProvider.FIREBASE.name()))
                    .data(eventData.getPushData())
                    .userTokens(new ArrayList<>(tokens))
                    .build();

            log.info("Sending push notification to tokens={}, request={}", tokens, pushNotificationRequest);

            notificationService.sendNotification(pushNotificationRequest);
        } catch(Exception e) {
            log.warn("Could not send a push notification with eventData={}", event.getSource(), e);
        }
    }
}