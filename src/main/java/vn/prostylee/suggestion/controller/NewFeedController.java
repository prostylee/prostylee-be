package vn.prostylee.suggestion.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.suggestion.dto.filter.NewFeedFilter;
import vn.prostylee.suggestion.dto.response.NewFeedResponse;
import vn.prostylee.suggestion.service.NewFeedService;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/new-feeds")
public class NewFeedController {

    private final NewFeedService newFeedService;

    public Page<NewFeedResponse> getNewFeed(@Valid NewFeedFilter newFeedFilter) {
        return this.newFeedService.getNewFeed(newFeedFilter);
    }
}
