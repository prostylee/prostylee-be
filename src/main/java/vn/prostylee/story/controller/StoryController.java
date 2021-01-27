package vn.prostylee.story.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.notification.dto.response.EmailTemplateResponse;
import vn.prostylee.story.dto.filter.StoryFilter;
import vn.prostylee.story.dto.request.StoryRequest;
import vn.prostylee.story.dto.response.StoryResponse;
import vn.prostylee.story.service.StoryService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/stories")
public class StoryController extends CrudController<StoryRequest, StoryResponse, Long, StoryFilter> {

    private StoryService service;

    @Autowired
    public StoryController(StoryService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/user")
    public Page<StoryResponse> getUserStoriesByUserId(StoryFilter baseFilter) {
        return service.getUserStoriesByUserId(baseFilter);
    }

    @GetMapping("/store")
    public Page<StoryResponse> getStoreStoriesByUserId(StoryFilter baseFilter) {
        return service.getStoreStoriesByUserId(baseFilter);
    }
}
