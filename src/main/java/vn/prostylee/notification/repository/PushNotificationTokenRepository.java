package vn.prostylee.notification.repository;

// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.auth.entity.Account;
import vn.prostylee.notification.entity.PushNotificationToken;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for domain model class Role.
 * @see PushNotificationToken;
 * @author prostylee
 */
@Repository
public interface PushNotificationTokenRepository extends BaseRepository<PushNotificationToken, Long> {

    Optional<PushNotificationToken> findByToken(String token);

    List<PushNotificationToken> getAllByAccount(Account account);

    @Query("SELECT new vn.prostylee.auth.dto.UserToken(t.account.id, t.token) " +
            "FROM PushNotificationToken t INNER JOIN t.account a ON a.id = t.account.id " +
            "WHERE t.account.id = :accountId AND (a.allowNotification is null OR a.allowNotification = true)")
    List<UserToken> getTokensByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT new vn.prostylee.auth.dto.UserToken(t.account.id, t.token) " +
            "FROM PushNotificationToken t INNER JOIN t.account a ON a.id = t.account.id INNER JOIN a.roles r " +
            "WHERE r.code IN (:roleCodes) AND (a.allowNotification is null OR a.allowNotification = true)")
    List<UserToken> getTokensByRoleCodes(@Param("roleCodes") List<String> roles);
}
