package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import vn.prostylee.auth.constant.AuthRole;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.converter.ZaloConverter;
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.ZaloResponse;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.exception.AuthenticationException;
import vn.prostylee.auth.repository.RoleRepository;
import vn.prostylee.auth.service.SocialAuthService;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.converter.GenderConverter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The helper class for implement login with Zalo
 */
@Component
@RequiredArgsConstructor
public class ZaloAuthServiceImpl implements SocialAuthService {
    /**
     * https://developers.zalo.me/docs/api/open-api/tai-lieu/thong-tin-nguoi-dung-post-28
     */
    private static final String REQUIRED_FIELDS_FOR_PARAMS = "id,birthday,name,gender,picture.type(large)";
    private static final String ACCESS_TOKEN_KEY = "access_token";
    private static final String FIELDS_KEY = "fields";
    private static final String PROVIDER_NAME = "zalo";

    @Value("${services.zalo.endpoint}")
    private String endpoint;

    @Value("${services.zalo.version}")
    private String version;

    @Value("${services.zalo.path.profile}")
    private String profilePath;

    private final UserLinkAccountService userLinkAccountService;

    private final UserService userService;

    private final RoleRepository roleRepository;

    private final WebClient client;

    @Override
    public boolean canHandle(SocialProviderType type) {
        return type == SocialProviderType.ZALO;
    }

    @Override
    public AuthUserDetails login(LoginSocialRequest request) {
        ZaloResponse response = call(client, request.getIdToken());
        if (Objects.isNull(response)) {
            throw new AuthenticationException("Can not login to the system, Please contact administrator!");
        }

        Optional<UserLinkAccount> userLinkAccount = userLinkAccountService.getUserLinkAccountBy(response.getId());

        if (userLinkAccount.isPresent()) {
            return processExist(userLinkAccount);
        }
        return processNew(response);
    }

    private AuthUserDetails processNew(ZaloResponse response) {
        User user = buildUser(response);
        User savedUser = userService.save(user);
        return new AuthUserDetails(user, getFeatures(savedUser));
    }

    private AuthUserDetails processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        return new AuthUserDetails(user, getFeatures(user));
    }

    private User buildUser(ZaloResponse zaloResponse) {
        User user = new User();
        user.setActive(true);
        user.setAllowNotification(true);
        user.setFullName(zaloResponse.getName());
        user.setUsername(zaloResponse.getId());
        user.setGender(GenderConverter.convertGender(zaloResponse.getGender()));
        user.setAvatar(zaloResponse.getPicture());
        String birthDay = Optional.ofNullable(zaloResponse.getBirthday()).orElse(StringUtils.EMPTY);
        user.setDate(ZaloConverter.convertDay(birthDay));
        user.setMonth(ZaloConverter.convertMonth(birthDay));
        user.setYear(ZaloConverter.convertYear(birthDay));
        Set<UserLinkAccount> sets = buildUserLinkAccounts(zaloResponse, user);
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

    private Set<UserLinkAccount> buildUserLinkAccounts(ZaloResponse zaloResponse, User user) {
        Set<UserLinkAccount> set = new HashSet<>();
        UserLinkAccount userLinkAccount = UserLinkAccount.builder()
                .user(user)
                .providerId(zaloResponse.getId())
                .providerName(PROVIDER_NAME)
                .build();
        set.add(userLinkAccount);
        return set;
    }

    private ZaloResponse call(WebClient client, String idToken) {
        return client.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path(endpoint + "/{version}/{endpoint}")
                        .queryParam(ACCESS_TOKEN_KEY, idToken)
                        .queryParam(FIELDS_KEY, REQUIRED_FIELDS_FOR_PARAMS)
                        .build(version, profilePath))
                .retrieve()
                .bodyToMono(ZaloResponse.class)
                .block();
    }
}
