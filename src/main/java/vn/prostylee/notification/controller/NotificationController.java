package vn.prostylee.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.notification.dto.filter.NotificationFilter;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.notification.service.NotificationService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public Page<NotificationResponse> getAll(NotificationFilter baseFilter) {
        return notificationService.findAll(baseFilter);
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable Long id) {
        return notificationService.findById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public SimpleResponse delete(@PathVariable Long id) {
        return SimpleResponse.builder().data(notificationService.deleteById(id)).build();
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public SimpleResponse deleteAll() {
        return SimpleResponse.builder().data(notificationService.deleteAll()).build();
    }

    @PutMapping("/{id}")
    public SimpleResponse markAsRead(@PathVariable Long id) {
        return SimpleResponse.builder().data(notificationService.markAsRead(id)).build();
    }

    @PutMapping
    public SimpleResponse markAllAsRead() {
        return SimpleResponse.builder().data(notificationService.markAllAsRead()).build();
    }

    @GetMapping("/count")
    public SimpleResponse countUnreadNotification() {
        return SimpleResponse.builder().data(notificationService.countUnreadNotification()).build();
    }
}
