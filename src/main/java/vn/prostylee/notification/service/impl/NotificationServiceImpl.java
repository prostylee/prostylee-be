package vn.prostylee.notification.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.constant.NotificationType;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.ExpoPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmPushNotificationRequest;
import vn.prostylee.notification.dto.request.FcmSubscriptionRequest;
import vn.prostylee.notification.dto.request.NotificationRequest;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.notification.entity.Notification;
import vn.prostylee.notification.factory.PushNotificationServiceFactory;
import vn.prostylee.notification.repository.NotificationRepository;
import vn.prostylee.notification.service.NotificationService;

import javax.persistence.criteria.Join;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final AuthenticatedProvider authenticatedProvider;

    private final NotificationRepository notificationRepository;

    private final BaseFilterSpecs<Notification> baseFilterSpecs;


    @Override
    public Page<NotificationResponse> findAll(BaseFilter baseFilter) {
        Specification<Notification> searchable = baseFilterSpecs.search(baseFilter);
        Specification<Notification> additionalSpec = (root, query, cb) -> cb.equal(root.get("userId"), authenticatedProvider.getUserId().get());
        searchable = searchable.and(additionalSpec);
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Notification> page = notificationRepository.findAll(searchable, pageable);
        return page.map(entity -> BeanUtil.copyProperties(entity, NotificationResponse.class));
    }

    @Override
    public NotificationResponse findById(Long id) {
        Notification tripCatalog = getById(id);
        return BeanUtil.copyProperties(tripCatalog, NotificationResponse.class);
    }

    private Notification getById(Long id) {
        return notificationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Trip catalog is not found with id [" + id + "]"));
    }

    @Override
    public NotificationResponse save(NotificationRequest request) {
        Notification notification = BeanUtil.copyProperties(request, Notification.class);
        if (request.getData() != null) {
            notification.setAdditionalData(JsonUtils.toJson(request.getData()));
        }
        notification.setUserId(request.getUserId());
        Notification savedNotification = notificationRepository.save(notification);
        return BeanUtil.copyProperties(savedNotification, NotificationResponse.class);
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification notification = getById(id);
        if (request.getData() != null) {
            notification.setAdditionalData(JsonUtils.toJson(request.getData()));
        }
        BeanUtil.mergeProperties(request, notification);
        Notification savedNotification = notificationRepository.save(notification);
        return BeanUtil.copyProperties(savedNotification, NotificationResponse.class);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            notificationRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public boolean markAsRead(Long id) {
        Notification notification = getById(id);
        notification.setMarkAsRead(true);
        notificationRepository.save(notification);
        return true;
    }

    @Override
    public boolean markAllAsRead() {
        Long userId = authenticatedProvider.getUserId().get();
        notificationRepository.markAllAsReadByUserId(userId, LocalDateTime.now());
        return true;
    }

    @Override
    public int saveAll(List<NotificationRequest> notificationRequests) {
        return ChunkServiceExecutor.execute(notificationRequests, subNotificationRequests -> {
            subNotificationRequests.forEach(this::save);
            notificationRepository.flush();
            return subNotificationRequests.size();
        });
    }

    @Override
    public boolean deleteAll() {
        Long userId = authenticatedProvider.getUserId().get();
        notificationRepository.deleteAllByUserId(userId);
        return true;
    }

    @Override
    public boolean sendNotification(PushNotificationDto request) {
        sendPushNotification(request);
        return true;
    }

    @Override
    public int countUnreadNotification() {
        Long userId = authenticatedProvider.getUserId().get();
        return notificationRepository.countUnreadNotification(userId);
    }

    private void sendPushNotification(PushNotificationDto request) {
        NotificationType.getByType(request.getType()).ifPresent(notificationType -> {
            switch (notificationType.getProvider()){
                case EXPO:
                    sendViaExpoPush(request, notificationType.getProvider());
                    break;
                case FIREBASE:
                    sendViaFcm(request, notificationType.getProvider());
                    break;
                default:
                    break;
            }
        });
    }

    private void sendViaExpoPush(PushNotificationDto request, NotificationProvider provider) {
        String[]tokens = request.getUserTokens().stream().map(UserToken::getToken).distinct().toArray(String[]::new);
        ExpoPushNotificationRequest pushNotificationRequest = ExpoPushNotificationRequest.builder()
                .to(tokens)
                .title(request.getTitle())
                .body(request.getBody())
                .data(request.getData())
                .build();
        PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(pushNotificationRequest);
    }

    private void sendViaFcm(PushNotificationDto request, NotificationProvider provider) {
        List<String> tokens = request.getUserTokens().stream().map(UserToken::getToken).distinct().collect(Collectors.toList());
        FcmPushNotificationRequest.FcmPushNotificationRequestBuilder notificationRequest = FcmPushNotificationRequest.builder()
                .tokens(tokens)
                .title(request.getTitle())
                .body(request.getBody())
                .data(request.getData());
        if (StringUtils.isNotBlank(request.getTopicName())) {
            FcmSubscriptionRequest subscriptionRequest = FcmSubscriptionRequest.builder()
                    .topicName(request.getTopicName())
                    .build();
            subscriptionRequest.withParentBuilder(notificationRequest);
            PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(subscriptionRequest);
        } else {
            PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(notificationRequest.build());
        }
    }
}
