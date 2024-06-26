package vn.prostylee.notification.service;

import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.notification.dto.request.SubscribePushNotificationRequest;

import java.util.List;

public interface PushNotificationTokenService {

    boolean subscribe(SubscribePushNotificationRequest request);

    List<UserToken> getTokensByUserId(Long userId);

    List<UserToken> getTokensByUserIds(List<Long> userIds);

    List<UserToken> getTokensByStoreId(Long storeId);

    List<UserToken> getTokensByStoreIds(List<Long>  storeIds);
}
