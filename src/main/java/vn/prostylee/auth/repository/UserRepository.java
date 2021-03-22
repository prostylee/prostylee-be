package vn.prostylee.auth.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.prostylee.auth.entity.User;
import vn.prostylee.core.repository.BaseRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for domain model class Account.
 * @see User ;
 * @author prostylee
 */
public interface UserRepository extends BaseRepository<User, Long> {

    @Query("SELECT e FROM #{#entityName} e where e.username= :username AND e.active = true AND e.deletedAt is null")
    Optional<User> findByActivatedUsername(@Param("username") String username);

    @Query("SELECT e FROM #{#entityName} e where e.email= :email AND e.active = true AND e.deletedAt is null")
    Optional<User> findActivatedUserByEmail(@Param("email") String email);

    @Query("SELECT e FROM #{#entityName} e where e.username = :username")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT e FROM #{#entityName} e where e.sub = :sub AND e.active = true AND e.deletedAt is null")
    Optional<User> findActivatedUserBySub(@Param("sub") String sub);

    @Query("SELECT e FROM #{#entityName} e where e.id in :ids")
    List<User> findUsersByIds(@Param("ids") List<Long> ids);
}
