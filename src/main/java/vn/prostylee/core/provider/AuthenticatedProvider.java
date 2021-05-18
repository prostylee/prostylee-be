package vn.prostylee.core.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import vn.prostylee.auth.dto.response.UserCredential;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.repository.UserRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthenticatedProvider {

    private final UserRepository userRepository;

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
            findUserIdBySub(userCredential.getSub()).ifPresent(userCredential::setId);
        }
        return Optional.ofNullable(userCredential);
    }

    private Optional<Long> findUserIdBySub(String sub) {
        if (StringUtils.isNotBlank(sub)) {
            return userRepository.findActivatedUserBySub(sub).map(User::getId);
        }
        return Optional.empty();
    }
}
