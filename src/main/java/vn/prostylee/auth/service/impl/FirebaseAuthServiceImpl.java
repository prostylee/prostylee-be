package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.service.AuthenticationService;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * The helper class for implement login with Firebase
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthServiceImpl extends AuthenticationServiceCommon implements AuthenticationService {
    private static final String PICTURE_RESPONSE_KEY = "picture";
    private static final String USER_ID_RESPONSE_KEY = "user_id";
    private static final String SIGN_IN_PROVIDER_RESPONSE_KEY = "sign_in_provider";
    private static final String FIREBASE_RESPONSE_KEY = "firebase";

    @Override
    public boolean canHandle(SocialProviderType type) {
        return type == SocialProviderType.FIREBASE;
    }

    private final UserLinkAccountService userLinkAccountService;

    private final UserService userService;

    @Override
    public JwtAuthenticationToken login(LoginSocialRequest request) {
        FirebaseToken fireBaseToken = null;
        try {
            fireBaseToken = FirebaseAuth.getInstance().verifyIdToken(request.getIdToken());
        } catch (FirebaseAuthException e) {
           log.error("Firebase verify token has problem:", e);
        }

        if(ObjectUtils.isNotEmpty(fireBaseToken)){
            Map<String, Object> claims = fireBaseToken.getClaims();
            String userId = String.valueOf(claims.get("user_id"));
            Optional<UserLinkAccount> linkAccount = userLinkAccountService.getUserLinkAccountBy(userId);
            if(linkAccount.isPresent()) {
                return processExist(linkAccount);
            }
            return processNew(fireBaseToken);

        }
        return new JwtAuthenticationToken();
    }

    private JwtAuthenticationToken processNew(FirebaseToken firebaseToken) {
        User user  = buildUser(firebaseToken);
        userService.save(user);
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private JwtAuthenticationToken processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private User buildUser(FirebaseToken firebaseToken) {
        User user = new User();
        user.setEmail(firebaseToken.getEmail());
        user.setActive(true);
        user.setAllowNotification(true);
        user.setFullName(firebaseToken.getName());
        user.setUsername(firebaseToken.getEmail());
        user.setAvatar(getPicture(firebaseToken));
        Set<UserLinkAccount> sets = buildUserLinkAccounts(firebaseToken, user);
        user.setUserLinkAccounts(sets);
        return user;
    }

    private String getPicture(FirebaseToken firebaseToken) {
        return String.valueOf(firebaseToken.getClaims().get(PICTURE_RESPONSE_KEY));
    }

    private Set<UserLinkAccount> buildUserLinkAccounts(FirebaseToken firebaseToken, User user) {
        Set<UserLinkAccount> set = new HashSet<>();
        Map<String, Object> claims = firebaseToken.getClaims();
        Map<String, Object> fireBases = (Map<String, Object>) claims.get(FIREBASE_RESPONSE_KEY);
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(String.valueOf(claims.get(USER_ID_RESPONSE_KEY)))
                .providerName(String.valueOf(fireBases.get(SIGN_IN_PROVIDER_RESPONSE_KEY)))
                .build();
        set.add(userLinkAccount);
        return set;
    }

}
