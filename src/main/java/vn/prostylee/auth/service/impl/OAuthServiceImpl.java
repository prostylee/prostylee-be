package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.request.OAuthRequest;
import vn.prostylee.auth.dto.request.OAuthUserInfoRequest;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.service.OAuthService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.converter.GenderConverter;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.notification.event.email.EmailEvent;
import vn.prostylee.notification.event.email.EmailEventDto;
import vn.prostylee.notification.constant.EmailTemplateType;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class OAuthServiceImpl implements OAuthService {

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public SimpleResponse signUp(OAuthRequest oAuthRequest) {
        log.debug("oAuthRequest {}", oAuthRequest);

        UserRequest userRequest = buildUserRequest(oAuthRequest);
        final String sub = oAuthRequest.getUserInfo().getSub();
        Optional<User> optUser = userService.findBySub(sub);
        UserResponse userResponse;
        if (optUser.isPresent()) {
            userResponse = userService.update(optUser.get().getId(), userRequest);
        } else {
            userResponse = userService.save(userRequest);
            this.sendEmailWelcome(userRequest.getEmail(), userRequest);
        }

        return SimpleResponse.builder()
                .data(userResponse)
                .build();
    }

    private UserRequest buildUserRequest(OAuthRequest oAuthRequest) {
        return UserRequest.builder()
                .sub(oAuthRequest.getUserInfo().getSub())
                .username(oAuthRequest.getUsername())
                .fullName(getFullName(oAuthRequest.getUserInfo()))
                .phoneNumber(oAuthRequest.getUserInfo().getPhoneNumber())
                .gender(GenderConverter.convertGender(oAuthRequest.getUserInfo().getGender()))
                .email(oAuthRequest.getUserInfo().getEmail())
                .roles(oAuthRequest.getGroups())
                .active(oAuthRequest.getEnabled())
                .emailVerified(oAuthRequest.getUserInfo().getEmailVerified())
                .phoneNumberVerified(oAuthRequest.getUserInfo().getPhoneNumberVerified())
                .build();
    }

    private String getFullName(OAuthUserInfoRequest userInfo) {
        if (StringUtils.isNotBlank(userInfo.getName())) {
            return userInfo.getName();
        }

        if (StringUtils.isNotBlank(userInfo.getFamilyName())
                || StringUtils.isNotBlank(userInfo.getGivenName())
                || StringUtils.isNotBlank(userInfo.getMiddleName())
        ) {
            return StringUtils.joinWith(" ", userInfo.getFamilyName(), userInfo.getMiddleName(), userInfo.getGivenName()).trim();
        }

        if (StringUtils.isNotBlank(userInfo.getNickname())) {
            return userInfo.getNickname();
        }

        return userInfo.getPreferredUsername();
    }

    private void sendEmailWelcome(String email, UserRequest userRequest) {
        EmailEventDto<?> eventDto = EmailEventDto.builder()
                .emailTemplateType(EmailTemplateType.WELCOME)
                .email(email)
                .data(userRequest)
                .build();
        eventPublisher.publishEvent(new EmailEvent(eventDto));
    }
}
