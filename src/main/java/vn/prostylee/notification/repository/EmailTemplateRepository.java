package vn.prostylee.notification.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.notification.entity.EmailTemplate;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for domain model class EmailTemplate.
 * @see EmailTemplate;
 * @author prostylee
 */
@Repository
public interface EmailTemplateRepository extends BaseRepository<EmailTemplate, Long> {

    Optional<EmailTemplate> findByType(String type);
}
