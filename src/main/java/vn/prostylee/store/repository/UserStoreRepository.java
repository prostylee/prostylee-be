package vn.prostylee.store.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.store.entity.UserStore;

import java.util.Optional;

/**
 * Repository for domain model class UserStore.
 * @see UserStore;
 * @author prostylee
 */
@Repository
public interface UserStoreRepository extends BaseRepository<UserStore, Long> {

    Optional<UserStore> findByUserId(Long userId);
}
