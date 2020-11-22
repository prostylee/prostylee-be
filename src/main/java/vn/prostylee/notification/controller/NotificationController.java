package vn.prostylee.notification.controller;

import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.notification.dto.filter.NotificationFilter;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public Boolean delete(@PathVariable Long id) {
        return notificationService.deleteById(id);
    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public Boolean deleteAll() {
        return notificationService.deleteAll();
    }

    @PutMapping("/{id}")
    public Boolean markAsRead(@PathVariable Long id) {
        return notificationService.markAsRead(id);
    }

    @PutMapping
    public Boolean markAllAsRead() {
        return notificationService.markAllAsRead();
    }

    @GetMapping("/count")
    public Integer countUnreadNotification() {
        return notificationService.countUnreadNotification();
    }
}
