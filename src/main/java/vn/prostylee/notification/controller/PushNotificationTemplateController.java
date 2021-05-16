package vn.prostylee.notification.controller;

import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.filter.PushNotificationTemplateFilter;
import vn.prostylee.notification.dto.request.PushNotificationTemplateDryRunRequest;
import vn.prostylee.notification.dto.request.PushNotificationTemplateRequest;
import vn.prostylee.notification.dto.response.PushNotificationTemplateResponse;
import vn.prostylee.notification.service.PushNotificationTemplateService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/push-notification-templates")
public class PushNotificationTemplateController extends CrudController<PushNotificationTemplateRequest, PushNotificationTemplateResponse, Long, PushNotificationTemplateFilter> {

    private final PushNotificationTemplateService service;

    public PushNotificationTemplateController(PushNotificationTemplateService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/{id}/dry-run")
    public PushNotificationDto tryToSend(@PathVariable Long id, @RequestBody PushNotificationTemplateDryRunRequest request) {
        return service.dryRun(id, request);
    }
}
