package vn.prostylee.notification.service;

import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.NotificationRequest;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.core.service.CrudService;

import java.util.List;

public interface NotificationService extends CrudService<NotificationRequest, NotificationResponse, Long> {

    boolean markAsRead(Long id);

    boolean markAllAsRead();

    int saveAll(List<NotificationRequest> notificationRequests);

    boolean deleteAll();

    boolean sendNotification(PushNotificationDto request);

    int countUnreadNotification();
}
