package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import java.util.Map;
import java.util.Optional;

/**
 * The helper class for implement login with Firebase
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthServiceImpl extends AuthenticationServiceCommon implements AuthenticationService {
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
        User user = userService.save(firebaseToken);
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private JwtAuthenticationToken processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }
}
