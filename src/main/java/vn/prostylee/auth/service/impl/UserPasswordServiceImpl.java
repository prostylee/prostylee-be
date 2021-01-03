package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.dto.request.ChangePasswordRequest;
import vn.prostylee.auth.dto.request.ForgotPasswordRequest;
import vn.prostylee.auth.dto.response.UserTempResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.service.UserPasswordService;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.auth.service.UserTempService;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.EncrytedPasswordUtils;
import vn.prostylee.notification.configure.event.EmailEvent;
import vn.prostylee.notification.configure.event.EmailEventDto;
import vn.prostylee.notification.constant.EmailTemplateType;

@Service
@RequiredArgsConstructor
public class UserPasswordServiceImpl implements UserPasswordService {

    private final UserTempService userTempService;

    private final UserService userService;

    private final AuthenticatedProvider authenticatedProvider;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public boolean forgotPassword(ForgotPasswordRequest request) {
        if (!userService.isExistsUserByEmail(request.getEmail())) {
            throw new ResourceNotFoundException("User is not exists by getting with email " + request.getEmail());
        }

        UserTempResponse userTempResponse = userTempService.createUserTemp(request.getEmail());
        sendEmailForgotPassword(request.getEmail(), userTempResponse);
        return true;
    }

    private void sendEmailForgotPassword(String email, UserTempResponse userTempResponse) {
        EmailEventDto<?> eventDto = EmailEventDto.builder()
                .emailTemplateType(EmailTemplateType.FORGOT_PASSWORD)
                .email(email)
                .data(userTempResponse)
                .build();
        eventPublisher.publishEvent(new EmailEvent(eventDto));
    }

    @Override
    public boolean changePassword(ChangePasswordRequest request) {
        String email = request.getEmail();
        final User user = userService.getUserByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User is not exists by getting with email " + email));

        if ((authenticatedProvider.getUserId().isPresent() && EncrytedPasswordUtils.isMatched(request.getPassword(), user.getPassword())) ||
                userTempService.isValid(email, request.getPassword())) {

            // Save a new password
            user.setPassword(EncrytedPasswordUtils.encryptPassword(request.getNewPassword()));
            userService.save(user);

            // Delete temp account
            userTempService.delete(email);

            return true;
        }
        throw new ResourceNotFoundException("Incorrect password or password is expired");
    }
}
