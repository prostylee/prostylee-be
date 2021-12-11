package vn.prostylee.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.notification.dto.request.ExpoPushNotificationRequest;
import vn.prostylee.notification.dto.request.SubscribePushNotificationRequest;
import vn.prostylee.notification.service.PushNotificationService;
import vn.prostylee.notification.service.PushNotificationTokenService;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/push-notifications")
public class PushNotificationController {

    private final PushNotificationService service;
    private final PushNotificationTokenService tokenService;

    @Autowired
    public PushNotificationController(
            @Qualifier("expoPushNotificationService") PushNotificationService<ExpoPushNotificationRequest> service,
            PushNotificationTokenService tokenService) {
        this.service = service;
        this.tokenService = tokenService;
    }

    @PostMapping("/subscribe")
    @ResponseStatus(code = HttpStatus.CREATED)
    public Boolean subscribe(@Valid @RequestBody SubscribePushNotificationRequest request) {
        tokenService.subscribe(request);
        return true;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Boolean send(@Valid @RequestBody ExpoPushNotificationRequest request) {
        service.sendPushNotificationAsync(request);
        return true;
    }
}
