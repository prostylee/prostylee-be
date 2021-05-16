package vn.prostylee.notification.repository;

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.notification.entity.PushNotificationTemplate;

import java.util.Optional;

/**
 * Repository for domain model class PushNotificationTemplate.
 * @see PushNotificationTemplate;
 * @author prostylee
 */
@Repository
public interface PushNotificationTemplateRepository extends BaseRepository<PushNotificationTemplate, Long> {

    Optional<PushNotificationTemplate> findByType(String type);
}
