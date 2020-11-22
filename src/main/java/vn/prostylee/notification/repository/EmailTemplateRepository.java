package vn.prostylee.notification.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

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

    Optional<EmailTemplate> findByTypeAndLanguage(String type, String language);
}
