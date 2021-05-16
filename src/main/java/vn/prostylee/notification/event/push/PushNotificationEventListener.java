package vn.prostylee.notification.event.push;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushNotificationEventListener implements ApplicationListener<PushNotificationEvent> {

    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;

    @Async
    @Override
    public void onApplicationEvent(PushNotificationEvent event) {
        log.info("Created instance: event={}", event.getSource());
        if (!(event.getSource() instanceof PushNotificationEventDto)) {
            log.warn("Can not handle email event with source type is {}", event.getSource());
            return;
        }

        try {
            PushNotificationEventDto<?> pushNotificationEventDto = (PushNotificationEventDto<?>) event.getSource();

            EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByType(pushNotificationEventDto.getEmailTemplateType().name());
            MailTemplateConfig config = MailTemplateConfig.builder()
                    .mailContent(emailTemplateResponse.getContent())
                    .mailSubject(emailTemplateResponse.getTitle())
                    .mailIsHtml(true)
                    .build();

            MailInfo mailInfo = new MailInfo();
            mailInfo.addTo(pushNotificationEventDto.getEmail());
            emailService.sendAsync(mailInfo, config, pushNotificationEventDto.getData());
        } catch(ResourceNotFoundException e) {
            log.warn("There is no email template for sending an email type {}", event.getSource(), e);
        }
    }
}