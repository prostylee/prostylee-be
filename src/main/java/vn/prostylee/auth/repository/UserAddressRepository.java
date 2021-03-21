package vn.prostylee.auth.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.auth.entity.Role;
import vn.prostylee.auth.entity.UserAddress;
import vn.prostylee.core.repository.BaseRepository;

/**
 * Repository for domain model class Role.
 * @see Role;
 * @author prostylee
 */
@Repository
public interface UserAddressRepository extends BaseRepository<UserAddress, Long> {
    UserAddress findOneByUserIdAndPriority(Long userId, Boolean priority);
}
