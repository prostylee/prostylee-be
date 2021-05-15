package vn.prostylee.notification.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.constant.CachingKey;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ApplicationException;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.exception.ValidatingException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.dto.mail.MailInfo;
import vn.prostylee.notification.dto.mail.MailTemplateConfig;
import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.entity.EmailTemplate;
import vn.prostylee.notification.repository.EmailTemplateRepository;
import vn.prostylee.notification.service.EmailService;
import vn.prostylee.notification.service.EmailTemplateService;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class EmailTemplateServiceImpl implements EmailTemplateService {

    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailService emailService;
    private final BaseFilterSpecs<EmailTemplate> baseFilterSpecs;

    @Override
    public Page<EmailTemplateResponse> findAll(BaseFilter baseFilter) {
        Specification<EmailTemplate> searchable = baseFilterSpecs.search(baseFilter);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<EmailTemplate> page = emailTemplateRepository.findAll(searchable, pageable);
        return page.map(this::convertToResponse);
    }

    @Cacheable(value = CachingKey.EMAIL_TEMPLATES, key = "#id")
    @Override
    public EmailTemplateResponse findById(Long id) {
        EmailTemplate emailTemplate = getById(id);
        return convertToResponse(emailTemplate);
    }

    @Override
    public EmailTemplateResponse save(EmailTemplateRequest request) {
        EmailTemplate emailTemplate = BeanUtil.copyProperties(request, EmailTemplate.class);
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        return convertToResponse(savedEmailTemplate);
    }

    private EmailTemplate getById(Long id) {
        return emailTemplateRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Email template is not found with id [" + id + "]"));
    }

    @CacheEvict(value = CachingKey.EMAIL_TEMPLATES, allEntries = true)
    @Override
    public EmailTemplateResponse update(Long id, EmailTemplateRequest request) {
        EmailTemplate emailTemplate = getById(id);
        BeanUtil.mergeProperties(request, emailTemplate);
        EmailTemplate savedEmailTemplate = emailTemplateRepository.save(emailTemplate);
        return convertToResponse(savedEmailTemplate);
    }

    @CacheEvict(value = CachingKey.EMAIL_TEMPLATES, allEntries = true)
    @Override
    public boolean deleteById(Long id) {
        try {
            emailTemplateRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a email template without existing in database", e);
            return false;
        }
    }

    @Cacheable(value = CachingKey.EMAIL_TEMPLATES, key = "#type")
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
