package vn.prostylee.notification.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;

import java.util.Map;

public interface EmailTemplateService extends CrudService<EmailTemplateRequest, EmailTemplateResponse, Long> {

    EmailTemplateResponse findByType(String type);

    String dryRun(Long id, Map<String, Object> data);
}
