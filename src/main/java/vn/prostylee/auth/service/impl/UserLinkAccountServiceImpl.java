package vn.prostylee.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.repository.UserLinkAccountRepository;
import vn.prostylee.auth.service.UserLinkAccountService;

import java.util.Optional;

@Service
public class UserLinkAccountServiceImpl implements UserLinkAccountService {
    private final UserLinkAccountRepository userLinkAccountRepository;

    @Autowired
    public UserLinkAccountServiceImpl(UserLinkAccountRepository repo){
        this.userLinkAccountRepository = repo;
    }

    @Override
    public Optional<UserLinkAccount> getUserLinkAccountBy(String Id) {
        return userLinkAccountRepository.getByProviderId(Id);
    }
}
