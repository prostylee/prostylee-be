package vn.prostylee.notification.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.LocaleConstant;
import vn.prostylee.core.exception.ApplicationException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.exception.ValidatingException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.entity.EmailTemplate;
import vn.prostylee.notification.repository.EmailTemplateRepository;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailService emailService;

    @Override
    public List<EmailTemplateResponse> findAll() {
        return emailTemplateRepository.findAll().stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public EmailTemplateResponse findById(Long id) {
        EmailTemplate emailTemplate = getById(id);
        return convertToResponse(emailTemplate);
    }

    private EmailTemplate getById(Long id) {
        return emailTemplateRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Email template is not found with id [" + id + "]"));
    }

    @Override
    public EmailTemplateResponse update(Long id, EmailTemplateRequest request) {
        EmailTemplate emailTemplate = getById(id);
        BeanUtil.mergeProperties(request, emailTemplate);
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        return convertToResponse(savedEmailTemplate);
    }

    @Override
    public EmailTemplateResponse findByType(String type) {
        EmailTemplate emailTemplate = emailTemplateRepository.findByType(type).orElseThrow(
                () -> new ResourceNotFoundException("Email template is not found with type [" + type + "]"));
        return convertToResponse(emailTemplate);
    }

    @Override
    public String dryRun(Long id, Map<String, Object> data) {
        Object email = data.get("email");
        if (email == null || StringUtils.isBlank(email.toString())) {
            throw new ValidatingException("Missing a received email");
        }
        try {
            EmailTemplateResponse emailTemplateResponse = findById(id);
            MailTemplateConfig config = MailTemplateConfig.builder()
                    .mailContent(emailTemplateResponse.getContent())
                    .mailSubject(emailTemplateResponse.getTitle())
                    .mailIsHtml(true)
                    .build();

            MailInfo mailInfo = new MailInfo();
            mailInfo.addTo(String.valueOf(email));
            emailService.send(mailInfo, config, data);
            return "Send successfully";
        } catch(Exception e) {
            throw new ApplicationException("Can not dry-run for sending an email with id=" + id + " and data=" + data, e);
        }
    }

    private EmailTemplateResponse convertToResponse(EmailTemplate emailTemplate) {
        return BeanUtil.copyProperties(emailTemplate, EmailTemplateResponse.class);
    }
}
