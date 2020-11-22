package vn.prostylee.notification.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.notification.entity.Notification;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * Repository for domain model class Notification.
 * @see Notification;
 * @author prostylee
 */
@Repository
public interface NotificationRepository extends BaseRepository<Notification, Long> {

    void deleteAllByAccountId(Long accountId);

    @Modifying
    @Query("UPDATE Notification e " +
            "SET e.markAsRead = true, e.updatedAt = :updatedAt, e.updatedBy = :accountId " +
            "WHERE e.account.id = :accountId AND (e.markAsRead is null OR e.markAsRead = false)")
    void markAllAsReadByAccountId(@Param("accountId") Long accountId, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT count(e.id) FROM Notification e WHERE e.account.id = :accountId AND (e.markAsRead is null OR e.markAsRead = false)")
    int countUnreadNotification(@Param("accountId") Long accountId);
}
