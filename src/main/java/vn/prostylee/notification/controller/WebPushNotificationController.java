package vn.prostylee.notification.controller;

import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.notification.dto.request.FcmPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmSubscriptionRequest;
import vn.prostylee.notification.service.FcmPushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/web-notifications")
public class WebPushNotificationController {

    private final FcmPushNotificationService notificationService;

    @Autowired
    public WebPushNotificationController(FcmPushNotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/token")
    public Boolean sendPnsToDevice(@Valid @RequestBody FcmPushNotificationRequest notificationRequestDto) {
        return notificationService.sendPnsToDevice(notificationRequestDto);
    }

    @PostMapping("/subscribe")
    public void subscribeToTopic(@Valid @RequestBody FcmSubscriptionRequest subscriptionRequestDto) {
        notificationService.subscribeToTopic(subscriptionRequestDto);
    }

    @PostMapping("/unsubscribe")
    public void unsubscribeFromTopic(FcmSubscriptionRequest subscriptionRequestDto) {
        notificationService.unsubscribeFromTopic(subscriptionRequestDto);
    }
    @PostMapping("/topic")
    public Boolean sendPnsToTopic(@Valid @RequestBody FcmSubscriptionRequest notificationRequestDto) {
        return notificationService.sendPnsToTopic(notificationRequestDto);
    }
}
