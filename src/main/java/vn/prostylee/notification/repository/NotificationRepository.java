package vn.prostylee.notification.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.notification.entity.Notification;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

/**
 * Repository for domain model class Notification.
 * @see Notification;
 * @author prostylee
 */
@Repository
@Transactional
public interface NotificationRepository extends BaseRepository<Notification, Long> {

    void deleteAllByUserId(Long userId);

    @Modifying
    @Query("UPDATE Notification e " +
            "SET e.markAsRead = true, e.updatedAt = :updatedAt, e.updatedBy = :userId " +
            "WHERE e.userId = :userId AND (e.markAsRead is null OR e.markAsRead = false)")
    void markAllAsReadByUserId(@Param("userId") Long userId, @Param("updatedAt") LocalDateTime updatedAt);

    @Query("SELECT count(e.id) FROM Notification e WHERE e.userId = :userId AND (e.markAsRead is null OR e.markAsRead = false)")
    int countUnreadNotification(@Param("userId") Long userId);
}
