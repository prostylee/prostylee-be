package vn.prostylee.app.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.app.dto.request.ContactFormRequest;
import vn.prostylee.app.service.SupportService;
import vn.prostylee.core.configuration.properties.EmailProperties;
import vn.prostylee.notification.constant.EmailTemplateType;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

@RequiredArgsConstructor
@Service
public class SupportServiceImpl implements SupportService {

    private final EmailProperties emailProperties;

    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;

    @Override
    public boolean supportFromContactForm(ContactFormRequest request) {
        ContactFormRequest safeRequest = ContactFormRequest.builder()
                .fullName(sanitize(request.getFullName()))
                .phoneNumber(sanitize(request.getPhoneNumber()))
                .subject(sanitize(request.getSubject()))
                .content(sanitize(request.getContent()))
                .email(request.getEmail())
                .build();

        EmailTemplateResponse emailTemplateResponse = emailTemplateService.findByType(EmailTemplateType.CONTACT.name());
        MailTemplateConfig config = MailTemplateConfig.builder()
                .mailContent(emailTemplateResponse.getContent())
                .mailSubject(emailTemplateResponse.getTitle())
                .mailIsHtml(true)
                .build();

        MailInfo mailInfo = new MailInfo();
        mailInfo.addTo(emailProperties.getReceiveContact());
        emailService.sendAsync(mailInfo, config, safeRequest);
        return true;
    }

    private String sanitize(String text) {
        return StringEscapeUtils.escapeHtml4(text);
    }
}
