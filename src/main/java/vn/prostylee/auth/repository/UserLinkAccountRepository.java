package vn.prostylee.auth.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.auth.entity.UserLinkAccount;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;

import java.util.Optional;

/**
 * Repository for domain model class UserLinkAccount.
 * @see UserLinkAccount;
 * @author prostylee
 */
@Repository
public interface UserLinkAccountRepository extends BaseRepository<UserLinkAccount, Long> {

    Optional<UserLinkAccount> getByProviderId(String providerId);
}
