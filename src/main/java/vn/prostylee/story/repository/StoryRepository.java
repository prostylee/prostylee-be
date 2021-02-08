package vn.prostylee.story.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.prostylee.core.repository.BaseRepository;
import vn.prostylee.story.dto.response.StoryResponse;
import vn.prostylee.story.entity.Story;

import java.util.List;

/**
 * Repository for domain model class Story.
 * @see Story ;
 * @author prostylee
 */
@Repository
public interface StoryRepository extends BaseRepository<Story, Long> {
    @Query(value = "SELECT * FROM story s WHERE s.created_at >= now() - INTERVAL '1 DAY' AND s.target_type = :targetType AND s.target_id IN (:ids)", nativeQuery = true)
    Page<Story> getStories(List<Long> ids, String targetType, Pageable pageable);
}
