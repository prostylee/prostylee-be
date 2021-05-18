package vn.prostylee.store.service;

import java.util.Optional;

public interface UserStoreService {

    Optional<Long> findStoreIdByUserId(Long userId);
}
