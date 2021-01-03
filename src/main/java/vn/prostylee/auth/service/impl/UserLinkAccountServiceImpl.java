package vn.prostylee.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.repository.UserLinkAccountRepository;
import vn.prostylee.auth.service.UserLinkAccountService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLinkAccountServiceImpl implements UserLinkAccountService {
    private final UserLinkAccountRepository userLinkAccountRepository;

    @Override
    public Optional<UserLinkAccount> getUserLinkAccountBy(String providerId) {
        return userLinkAccountRepository.getByProviderId(providerId);
    }
}
