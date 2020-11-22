package vn.prostylee.core.provider;


import vn.prostylee.auth.dto.response.UserCredential;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedProvider {

    public Optional<Long> getUserId() {
        return getUser().map(UserCredential::getId);
    }

    public Optional<UserCredential> getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserCredential)) {
            return Optional.empty();
        }
        return Optional.ofNullable( (UserCredential) authentication.getPrincipal() );
    }
}
