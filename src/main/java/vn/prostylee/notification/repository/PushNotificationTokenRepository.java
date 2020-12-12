package vn.prostylee.notification.repository;

// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.prostylee.auth.dto.UserToken;
import vn.prostylee.notification.entity.PushNotificationToken;

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

    List<PushNotificationToken> getAllByUserId(Long userId);

    @Query("SELECT new vn.prostylee.auth.dto.UserToken(t.userId, t.token) " +
            "FROM PushNotificationToken t " +
            "WHERE t.userId = :userId ")
    List<UserToken> getTokensByUserId(@Param("userId") Long userId);

    @Query("SELECT new vn.prostylee.auth.dto.UserToken(t.userId, t.token) " +
            "FROM PushNotificationToken t " +
            "WHERE t.storeId = :storeId ")
    List<UserToken> getTokensByStoreId(@Param("storeId") Long storeId);
}
