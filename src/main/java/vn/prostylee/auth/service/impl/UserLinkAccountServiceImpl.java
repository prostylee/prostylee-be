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

@Service
public class UserLinkAccountServiceImpl implements UserLinkAccountService {

    @Autowired
    private UserLinkAccountRepository userLinkAccountRepository;

    @Override
    public UserLinkAccount saveBy(FirebaseToken firebaseToken, User user) {
        Map<String, Object> claims = firebaseToken.getClaims();
        Map<String, Object> fireBases = (Map<String, Object>) claims.get("firebase");
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(String.valueOf(claims.get("user_id")))
                .providerName(String.valueOf(fireBases.get("sign_in_provider")))
                .build();
        return userLinkAccountRepository.save(userLinkAccount);
    }
}
