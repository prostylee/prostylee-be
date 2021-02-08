package vn.prostylee.story.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoreStoryResponse;
import vn.prostylee.story.dto.response.UserStoryResponse;

public interface StoryService extends CrudService<StoryRequest, UserStoryResponse, Long> {
    Page<UserStoryResponse> getUserStoriesByUserId(BaseFilter baseFilter);
    Page<StoreStoryResponse> getStoreStoriesByUserId(BaseFilter baseFilter);
}
