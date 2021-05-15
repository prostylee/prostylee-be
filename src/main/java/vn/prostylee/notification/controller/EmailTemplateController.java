package vn.prostylee.notification.controller;

import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.notification.dto.filter.EmailTemplateFilter;
import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailTemplateService;

import java.util.Map;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/mail-templates")
public class EmailTemplateController extends CrudController<EmailTemplateRequest, EmailTemplateResponse, Long, EmailTemplateFilter> {

    private final EmailTemplateService service;

    public EmailTemplateController(EmailTemplateService service) {
        super(service);
        this.service = service;
    }

    @PostMapping("/{id}/dry-run")
    public String tryToSend(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return service.dryRun(id, body);
    }
}
