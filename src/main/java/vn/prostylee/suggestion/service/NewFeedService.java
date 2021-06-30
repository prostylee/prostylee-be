package vn.prostylee.suggestion.service;

import org.springframework.data.domain.Page;
import vn.prostylee.suggestion.dto.filter.NewFeedFilter;
import vn.prostylee.suggestion.dto.response.NewFeedResponse;

public interface NewFeedService {

    Page<NewFeedResponse> getNewFeed(NewFeedFilter newFeedFilter);
}
