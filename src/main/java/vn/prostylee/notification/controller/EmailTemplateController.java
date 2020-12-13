package vn.prostylee.notification.controller;

import vn.prostylee.notification.dto.request.EmailTemplateRequest;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.notification.service.EmailTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/mail-templates")
public class EmailTemplateController {

    private final EmailTemplateService service;

    @Autowired
    public EmailTemplateController(EmailTemplateService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmailTemplateResponse> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public EmailTemplateResponse getById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public EmailTemplateResponse update(@PathVariable Long id, @Valid @RequestBody EmailTemplateRequest request) {
        return service.update(id, request);
    }

    @PostMapping("/{id}/dry-run")
    public String tryToSend(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return service.dryRun(id, body);
    }
}
