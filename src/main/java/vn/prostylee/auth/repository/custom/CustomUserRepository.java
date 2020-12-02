package vn.prostylee.auth.repository.custom;

import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface CustomUserRepository extends UserRepository {

    Optional<User> findByActivatedUsername(String username);

    Optional<User> findActivatedUserByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> findByPushToken(String pushToken);

    Page<User> getAllUsers(UserFilter baseFilter, Pageable pageable);
}
