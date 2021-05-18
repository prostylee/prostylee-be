package vn.prostylee.store.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.store.entity.UserStore;
import vn.prostylee.store.repository.UserStoreRepository;
import vn.prostylee.store.service.UserStoreService;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserStoreServiceImpl implements UserStoreService {

    private final UserStoreRepository repository;

    @Override
    public Optional<Long> findStoreIdByUserId(Long userId) {
        return repository.findByUserId(userId).map(UserStore::getStoreId);
    }
}
