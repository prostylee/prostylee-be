package vn.prostylee.auth.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.auth.entity.Role;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for domain model class Role.
 * @see Role;
 * @author prostylee
 */
@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Optional<Role> findByCode(String code);
}
