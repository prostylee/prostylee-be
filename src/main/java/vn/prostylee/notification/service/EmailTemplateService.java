package vn.prostylee.notification.service;

import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;

import java.util.List;
import java.util.Map;

public interface EmailTemplateService {
    List<EmailTemplateResponse> findAll();

    EmailTemplateResponse findById(Long id);

    EmailTemplateResponse update(Long id, EmailTemplateRequest s);

    EmailTemplateResponse findByTypeAndLanguage(String type, String language);

    String dryRun(Long id, Map<String, Object> data);
}
