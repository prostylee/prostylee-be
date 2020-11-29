package vn.prostylee.app.repository;
// Generated May 31, 2020, 11:28:53 PM by Hibernate Tools 5.2.12.Final

import vn.prostylee.app.entity.AppMetadata;
import vn.prostylee.core.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for domain model class KlMetadata.
 * @see AppMetadata ;
 * @author prostylee
 */
@Repository
public interface AppMetadataRepository extends BaseRepository<AppMetadata, Long> {

    Optional<AppMetadata> findByCode(String code);
}
