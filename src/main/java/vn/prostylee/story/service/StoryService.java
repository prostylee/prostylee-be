package vn.prostylee.story.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoryResponse;

public interface StoryService extends CrudService<StoryRequest, StoryResponse, Long> {
    Page<StoryResponse> getUserStoriesByUserId(BaseFilter baseFilter);
    Page<StoryResponse> getStoreStoriesByUserId(BaseFilter baseFilter);
}
