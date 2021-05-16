package vn.prostylee.notification.event.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailEventListener implements ApplicationListener<EmailEvent> {

    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;

    @Async
    @Override
    public void onApplicationEvent(EmailEvent event) {
        log.info("Created instance: event={}", event.getSource());
        if (!(event.getSource() instanceof EmailEventDto)) {
            log.warn("Can not handle email event with source type is {}", event.getSource());
            return;
        }

        try {
            EmailEventDto<?> eventData = (EmailEventDto<?>) event.getSource();

            EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByType(eventData.getEmailTemplateType().name());
            MailTemplateConfig config = MailTemplateConfig.builder()
                    .mailContent(emailTemplateResponse.getContent())
                    .mailSubject(emailTemplateResponse.getTitle())
                    .mailIsHtml(true)
                    .build();

            MailInfo mailInfo = new MailInfo();
            mailInfo.addTos(eventData.getEmails());
            emailService.sendAsync(mailInfo, config, eventData.getData());
        } catch(Exception e) {
            log.warn("Could not send an email with eventData={}", event.getSource(), e);
        }
    }
}