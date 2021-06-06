package vn.prostylee.core.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.dto.response.UserCredential;

import java.util.Optional;

@Slf4j
@Component
public class AuthenticatedProvider {

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
        return Optional.ofNullable(userCredential);
    }
}
