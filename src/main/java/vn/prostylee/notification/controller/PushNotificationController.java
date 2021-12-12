package vn.prostylee.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.SubscribePushNotificationRequest;
import vn.prostylee.notification.service.NotificationService;
import vn.prostylee.notification.service.PushNotificationTokenService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/push-notifications")
public class PushNotificationController {

    private final NotificationService service;
    private final PushNotificationTokenService tokenService;

    @PostMapping("/subscribe")
    @ResponseStatus(code = HttpStatus.CREATED)
    public SimpleResponse subscribe(@Valid @RequestBody SubscribePushNotificationRequest request) {
        return SimpleResponse.builder().data(tokenService.subscribe(request)).build();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SimpleResponse send(@Valid @RequestBody PushNotificationDto request) {
        service.sendNotification(request);
        return SimpleResponse.builder().data(true).build();
    }
}
