package vn.prostylee.story.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.story.dto.filter.StoryFilter;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoreStoryResponse;
import vn.prostylee.story.dto.response.UserStoryResponse;
import vn.prostylee.story.service.StoryService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/stories")
public class StoryController extends CrudController<StoryRequest, UserStoryResponse, Long, StoryFilter> {

    private final StoryService service;

    @Autowired
    public StoryController(StoryService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/user")
    public Page<UserStoryResponse> getUserStoriesByUserId(StoryFilter baseFilter) {
        return service.getUserStoriesByUserId(baseFilter);
    }

    @GetMapping("/store")
    public Page<StoreStoryResponse> getStoreStoriesByUserId(StoryFilter baseFilter) {
        return service.getStoreStoriesByUserId(baseFilter);
    }
}
