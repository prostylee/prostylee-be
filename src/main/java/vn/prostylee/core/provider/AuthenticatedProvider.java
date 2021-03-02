package vn.prostylee.core.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.converter.UserCredentialConverter;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.service.impl.ExtUserDetailsService;
import vn.prostylee.core.exception.ResourceNotFoundException;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticatedProvider {

    private final ExtUserDetailsService userDetailsService;
    private final UserCredentialConverter userCredentialConverter;

    public Optional<Long> getUserId() {
        return getUser().map(UserCredential::getId);
    }

    public Long getUserIdValue() {
        return getUser().map(UserCredential::getId).orElse(null);
    }

    public Optional<UserCredential> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserCredential)) {
            return Optional.empty();
        }
        UserCredential userCredential = (UserCredential) authentication.getPrincipal();
        if (userCredential != null && (userCredential.getId() == null || userCredential.getId() <= 0)) {
            return findUserBySub(userCredential.getSub());
        }
        return Optional.ofNullable(userCredential);
    }

    private Optional<UserCredential> findUserBySub(String sub) {
        if (StringUtils.isNotBlank(sub)) {
            try {
                return Optional.ofNullable(userDetailsService.loadUserBySub(sub))
                        .map(userCredentialConverter::convert);
            } catch (ResourceNotFoundException e) {
                log.error("Can not get user credential by user sub={}", sub, e);
            }
        }
        return Optional.empty();
    }
}
