package vn.prostylee.media.repository;
// Generated Nov 28, 2020, 9:47:00 PM by Hibernate Tools 5.2.12.Final

import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.media.entity.Attachment;

import java.util.List;

/**
 * Repository for domain model class Attachement.
 * @see Attachment;
 * @author prostylee
 */
@Repository
public interface AttachementRepository extends BaseRepository<Attachment, Long> {
    void deleteAttachementsByIdIn(List<Long> Ids);
}
