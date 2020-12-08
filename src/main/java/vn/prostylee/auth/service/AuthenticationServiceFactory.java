package vn.prostylee.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.exception.AuthenticationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceFactory {

    private final List<AuthenticationService> services;

    public AuthenticationService getService(SocialProviderType type) {
        return services.stream().filter(authenticationService -> authenticationService.canHandle(type))
                .findFirst().orElseThrow( () -> new AuthenticationException("Provider not support"  + type));
    }
}
