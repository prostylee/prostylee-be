package vn.prostylee.story.repository;

import org.springframework.data.domain.Page;
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

    Page<StoryResponse> getStoryByTargetIdInAndTargetType(List<Long> ids, String targetType);
}
