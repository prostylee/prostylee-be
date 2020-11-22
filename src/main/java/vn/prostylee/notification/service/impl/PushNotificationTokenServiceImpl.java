package vn.prostylee.notification.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.notification.dto.request.SubscribePushNotificationRequest;
import vn.prostylee.notification.entity.PushNotificationToken;
import vn.prostylee.notification.repository.PushNotificationTokenRepository;
import vn.prostylee.notification.service.PushNotificationTokenService;

import java.util.Arrays;
import java.util.List;

@Service
public class PushNotificationTokenServiceImpl implements PushNotificationTokenService {

    private final AuthenticatedProvider authenticatedProvider;

    private final PushNotificationTokenRepository pushNotificationTokenRepository;

    @Autowired
    public PushNotificationTokenServiceImpl(
            AuthenticatedProvider authenticatedProvider,
            PushNotificationTokenRepository pushNotificationTokenRepository) {
        this.authenticatedProvider = authenticatedProvider;
        this.pushNotificationTokenRepository = pushNotificationTokenRepository;
    }

    @Override
    public boolean subscribe(SubscribePushNotificationRequest request) {
        if (!pushNotificationTokenRepository.findByToken(request.getToken()).isPresent()) {
            Account account = new Account();
            account.setId(authenticatedProvider.getUserId().get());

            PushNotificationToken pushNotificationToken = BeanUtil.copyProperties(request, PushNotificationToken.class);
            pushNotificationToken.setAccount(account);
            pushNotificationTokenRepository.save(pushNotificationToken);
        }
        return true;
    }

    @Override
    public List<UserToken> getTokensByAccountId(Long accountId) {
        return pushNotificationTokenRepository.getTokensByAccountId(accountId);
    }

    @Override
    public List<UserToken> getTokensByRoles(String...roles) {
        return pushNotificationTokenRepository.getTokensByRoleCodes(Arrays.asList(roles));
    }
}
