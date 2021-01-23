package vn.prostylee.story.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
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
}
