package vn.prostylee.notification.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.PushNotificationTemplateDryRunRequest;
import vn.prostylee.notification.dto.request.PushNotificationTemplateRequest;
import vn.prostylee.notification.dto.response.PushNotificationTemplateResponse;

public interface PushNotificationTemplateService extends CrudService<PushNotificationTemplateRequest, PushNotificationTemplateResponse, Long> {

    PushNotificationTemplateResponse findByType(String type);

    PushNotificationDto dryRun(Long id, PushNotificationTemplateDryRunRequest request);
}
