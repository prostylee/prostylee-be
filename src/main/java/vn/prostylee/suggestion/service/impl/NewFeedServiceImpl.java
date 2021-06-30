package vn.prostylee.suggestion.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.suggestion.dto.filter.NewFeedFilter;
import vn.prostylee.suggestion.dto.response.NewFeedResponse;
import vn.prostylee.suggestion.service.NewFeedService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewFeedServiceImpl implements NewFeedService {
    @Override
    public Page<NewFeedResponse> getNewFeed(NewFeedFilter newFeedFilter) {
        return null;
    }
}
