package vn.prostylee.notification.service;

import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.notification.dto.request.SubscribePushNotificationRequest;

import java.util.List;

public interface PushNotificationTokenService {

    boolean subscribe(SubscribePushNotificationRequest request);

    List<UserToken> getTokensByAccountId(Long accountId);

    List<UserToken> getTokensByRoles(String... roles);
}
