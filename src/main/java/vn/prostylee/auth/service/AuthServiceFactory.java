package vn.prostylee.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.exception.AuthenticationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceFactory {

    private final List<SocialAuthService> services;

    public SocialAuthService getService(SocialProviderType type) {
        return services.stream()
                .filter(socialAuthService -> socialAuthService.canHandle(type))
                .findFirst()
                .orElseThrow( () -> new AuthenticationException("Provider not support"  + type));
    }
}
