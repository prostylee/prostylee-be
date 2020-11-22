package vn.prostylee.business.controller;

import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.business.dto.request.ContactFormRequest;
import vn.prostylee.business.service.SupportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/supports")
public class SupportController {

    private final SupportService service;

    @Autowired
    public SupportController(SupportService service) {
        this.service = service;
    }

    @PostMapping("/email")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Boolean createOrder(@Valid @RequestBody ContactFormRequest request) {
        return service.supportFromContactForm(request);
    }
}
