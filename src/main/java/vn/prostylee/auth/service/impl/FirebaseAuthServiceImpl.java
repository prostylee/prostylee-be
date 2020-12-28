package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.exception.InvalidJwtToken;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.SocialAuthService;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The helper class for implement login with Firebase
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class FirebaseAuthServiceImpl implements SocialAuthService {
    private static final String PICTURE_RESPONSE_KEY = "picture";
    private static final String USER_ID_RESPONSE_KEY = "user_id";
    private static final String SIGN_IN_PROVIDER_RESPONSE_KEY = "sign_in_provider";
    private static final String FIREBASE_RESPONSE_KEY = "firebase";

    private final UserLinkAccountService userLinkAccountService;

    private final UserService userService;

    private final RoleRepository roleRepository;

    @Override
    public boolean canHandle(SocialProviderType type) {
        return type == SocialProviderType.FIREBASE;
    }

    @Override
    public AuthUserDetails login(LoginSocialRequest request) {
        FirebaseToken fireBaseToken = getFirebaseToken(request.getIdToken());
        Map<String, Object> claims = fireBaseToken.getClaims();
        String userId = String.valueOf(claims.get("user_id"));
        Optional<UserLinkAccount> linkAccount = userLinkAccountService.getUserLinkAccountBy(userId);
        if (linkAccount.isPresent()) {
            return processExist(linkAccount);
        }
        return processNew(fireBaseToken);
    }

    private FirebaseToken getFirebaseToken(String idToken) {
        try {
            return FirebaseAuth.getInstance().verifyIdToken(idToken);
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            throw new InvalidJwtToken("Firebase verify token has problem", e);
        }
    }

    private AuthUserDetails processNew(FirebaseToken firebaseToken) {
        User user = buildUser(firebaseToken);
        User savedUser = userService.save(user);
        return new AuthUserDetails(user, getFeatures(savedUser));
    }

    private AuthUserDetails processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        return new AuthUserDetails(user, getFeatures(user));
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
        user.setRoles(buildDefaultRole(AuthRole.BUYER));
        return user;
    }

    private Set<Role> buildDefaultRole(AuthRole defaultRole) {
        return Stream.of(roleRepository.findByCode(defaultRole.name()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private String getPicture(FirebaseToken firebaseToken) {
        return String.valueOf(firebaseToken.getClaims().get(PICTURE_RESPONSE_KEY));
    }

    private Set<UserLinkAccount> buildUserLinkAccounts(FirebaseToken firebaseToken, User user) {
        Set<UserLinkAccount> userLinkAccounts = new HashSet<>();
        Map<String, Object> claims = firebaseToken.getClaims();
        Map<String, Object> fireBases = (Map<String, Object>) claims.get(FIREBASE_RESPONSE_KEY);
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(String.valueOf(claims.get(USER_ID_RESPONSE_KEY)))
                .providerName(String.valueOf(fireBases.get(SIGN_IN_PROVIDER_RESPONSE_KEY)))
                .build();
        userLinkAccounts.add(userLinkAccount);
        return userLinkAccounts;
    }

}
