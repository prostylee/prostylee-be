package vn.prostylee.auth.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.repository.BaseRepository;

import java.util.Optional;

/**
 * Repository for domain model class Account.
 * @see User ;
 * @author prostylee
 */
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByActivatedUsername(String username);

    Optional<User> findActivatedUserByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByPushToken(String pushToken);

    Page<User> getAllUsers(UserFilter baseFilter, Pageable pageable);

    Optional<User> findActivatedUserBySub(String sub);
}
