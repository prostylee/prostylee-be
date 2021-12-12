package vn.prostylee.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.executor.ChunkServiceExecutor;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.notification.configuration.PushNotificationProperties;
import vn.prostylee.notification.constant.NotificationProvider;
import vn.prostylee.notification.dto.PushNotificationDto;
import vn.prostylee.notification.dto.request.*;
import vn.prostylee.notification.dto.response.NotificationDiscountResponse;
import vn.prostylee.notification.dto.response.NotificationResponse;
import vn.prostylee.notification.dto.response.NotificationSender;
import vn.prostylee.notification.entity.Notification;
import vn.prostylee.notification.factory.PushNotificationServiceFactory;
import vn.prostylee.notification.repository.NotificationRepository;
import vn.prostylee.notification.service.NotificationService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final AuthenticatedProvider authenticatedProvider;

    private final UserService userService;

    private final NotificationRepository notificationRepository;

    private final BaseFilterSpecs<Notification> baseFilterSpecs;

    private final PushNotificationProperties pushNotificationProperties;

    @Override
    public Page<NotificationResponse> findAll(BaseFilter baseFilter) {
        return findAll(authenticatedProvider.getUserIdValue(), baseFilter);
    }

    private Page<NotificationResponse> findAll(Long userId, BaseFilter baseFilter) {
        Specification<Notification> searchable = baseFilterSpecs.search(baseFilter);
        if (userId != null) {
            Specification<Notification> additionalSpec = (root, query, cb) -> cb.equal(root.get("userId"), userId);
            searchable = searchable.and(additionalSpec);
        }
        Pageable pageable = baseFilterSpecs.page(baseFilter);
        Page<Notification> page = notificationRepository.findAll(searchable, pageable);
        return page.map(this::toResponse);
    }

    private NotificationResponse toResponse(Notification entity) {
        NotificationResponse response = BeanUtil.copyProperties(entity, NotificationResponse.class);
        if (StringUtils.isNotBlank(entity.getAdditionalData())) {
            response.setData(JsonUtils.fromJson(entity.getAdditionalData(), HashMap.class));
        }
        if (entity.getCreatedBy() != null) {
            userService.getBasicUserInfo(entity.getCreatedBy()).ifPresent(
                    user -> response.setSender(NotificationSender.builder()
                            .userId(user.getId())
                            .avatar(user.getAvatar())
                            .name(user.getFullName())
                            .build()));
        }
        return response;
    }

    @Override
    public NotificationResponse findById(Long id) {
        Notification notification = getById(id);
        return toResponse(notification);
    }

    private Notification getById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip catalog is not found with id [" + id + "]"));
    }

    @Override
    public NotificationResponse save(NotificationRequest request) {
        Notification notification = BeanUtil.copyProperties(request, Notification.class);
        if (request.getData() != null) {
            notification.setAdditionalData(JsonUtils.toJson(request.getData()));
        }
        notification.setUserId(request.getUserId());
        Notification savedNotification = notificationRepository.save(notification);
        return toResponse(savedNotification);
    }

    @Override
    public NotificationResponse update(Long id, NotificationRequest request) {
        Notification notification = getById(id);
        if (request.getData() != null) {
            notification.setAdditionalData(JsonUtils.toJson(request.getData()));
        }
        BeanUtil.mergeProperties(request, notification);
        Notification savedNotification = notificationRepository.save(notification);
        return toResponse(savedNotification);
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            notificationRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Delete a notification without existing in database", e);
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
        Long userId = authenticatedProvider.getUserIdValue();
        notificationRepository.markAllAsReadByUserId(userId, new Date());
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
        Long userId = authenticatedProvider.getUserIdValue();
        notificationRepository.deleteAllByUserId(userId);
        return true;
    }

    @Override
    public boolean sendNotification(PushNotificationDto request) {
        saveToDb(request);
        sendPushNotification(request);
        return true;
    }

    @Override
    public int countUnreadNotification() {
        Long userId = authenticatedProvider.getUserIdValue();
        return notificationRepository.countUnreadNotification(userId);
    }

    @Override
    public Page<NotificationDiscountResponse> getNotificationDiscounts(BaseFilter filter) {
        // TODO Get from voucher/discount table
        // TODO Get special discounts by group user
        Page<NotificationResponse> responses = findAll(null, filter);
        return responses.map(notification -> BeanUtil.copyProperties(notification, NotificationDiscountResponse.class))
                .map(notification -> {
                    notification.setTitle("Ưu đãi toàn bộ sản phẩm");
                    return notification;
                });
    }

    private void saveToDb(PushNotificationDto request) {
        List<NotificationRequest> notificationRequests = request.getUserTokens().stream()
                .map(UserToken::getUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream()
                .map(userId -> toNotificationRequest(userId, request))
                .collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(notificationRequests)) {
            saveAll(notificationRequests);
        }
    }

    private NotificationRequest toNotificationRequest(Long userId, PushNotificationDto request) {
        return NotificationRequest.builder()
                .title(request.getTitle())
                .content(request.getBody())
                .data(request.getData())
                .userId(userId)
                .build();
    }

    private void sendPushNotification(PushNotificationDto request) {
        String providerName = Optional.ofNullable(request.getProvider())
                .orElse(pushNotificationProperties.getProvider());

        NotificationProvider.findProvider(providerName).ifPresent(provider -> {
            switch (provider) {
                case EXPO:
                    sendViaExpoPush(request, provider);
                    break;
                case FIREBASE:
                    sendViaFcm(request, provider);
                    break;
                case AWS_PINPOINT:
                    sendViaAwsPinpoint(request);
                    break;
                default:
                    log.warn("Unsupported provider={}", request.getProvider());
                    break;
            }
        });
    }

    private void sendViaExpoPush(PushNotificationDto request, NotificationProvider provider) {
        ExpoPushNotificationRequest pushNotificationRequest = ExpoPushNotificationRequest.builder()
                .to(extractTokens(request.getUserTokens()))
                .title(request.getTitle())
                .body(request.getBody())
                .data(request.getData())
                .build();

        log.debug("Push data={}", pushNotificationRequest);
        PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(pushNotificationRequest);
    }

    private void sendViaFcm(PushNotificationDto request, NotificationProvider provider) {
        List<String> tokens = extractTokens(request.getUserTokens());
        FcmPushNotificationRequest.FcmPushNotificationRequestBuilder notificationRequest = FcmPushNotificationRequest.builder()
                .to(tokens)
                .title(request.getTitle())
                .body(request.getBody())
                .data(request.getData());

        if (StringUtils.isNotBlank(request.getTopicName())) {
            FcmSubscriptionRequest subscriptionRequest = FcmSubscriptionRequest.builder()
                    .topicName(request.getTopicName())
                    .build();
            subscriptionRequest.withParentBuilder(notificationRequest);
            log.debug("Push data={}", subscriptionRequest);
            PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(subscriptionRequest);
        } else {
            log.debug("Push data={}", notificationRequest.build());
            PushNotificationServiceFactory.getService(provider).sendPushNotificationAsync(notificationRequest.build());
        }
    }

    private void sendViaAwsPinpoint(PushNotificationDto request) {
        AwsPushNotificationRequest pushNotificationRequest = AwsPushNotificationRequest.builder()
                .to(extractTokens(request.getUserTokens()))
                .title(request.getTitle())
                .body(request.getBody())
                .data(request.getData())
                .link(request.getLink())
                .silentPush(request.getSilentPush())
                .build();

        log.debug("Push data={}", pushNotificationRequest);
        PushNotificationServiceFactory.getService(NotificationProvider.AWS_PINPOINT).sendPushNotificationAsync(pushNotificationRequest);
    }

    private List<String> extractTokens(List<UserToken> userTokens) {
        return userTokens.stream()
                .map(UserToken::getToken)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }
}
