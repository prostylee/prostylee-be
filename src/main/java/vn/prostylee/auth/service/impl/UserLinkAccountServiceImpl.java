package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.repository.UserLinkAccountRepository;
import vn.prostylee.auth.service.UserLinkAccountService;

import java.util.Map;
import java.util.Optional;

@Service
public class UserLinkAccountServiceImpl implements UserLinkAccountService {

    @Autowired
    private UserLinkAccountRepository userLinkAccountRepository;

    @Override
    public Optional<UserLinkAccount> getUserLinkAccountBy(FirebaseToken firebaseToken) {
        Map<String, Object> claims = firebaseToken.getClaims();
        String userId = String.valueOf(claims.get("user_id"));
        return userLinkAccountRepository.getByProviderId(userId);
    }
}
