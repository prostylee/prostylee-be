package vn.prostylee.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.exception.AuthenticationException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceFactory {

    private final List<AuthenticationService> services;

    public AuthenticationService getService(SocialProviderType type) {
        return services.stream().filter(authenticationService -> authenticationService.getProviderType() == type)
                .findFirst().orElseThrow( () -> new AuthenticationException("Provider not support"));
    }
}
