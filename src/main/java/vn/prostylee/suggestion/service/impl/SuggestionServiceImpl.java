package vn.prostylee.suggestion.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.DbUtil;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.store.repository.StoreRepository;
import vn.prostylee.suggestion.dto.filter.SuggestionKeywordFilter;
import vn.prostylee.suggestion.service.SuggestionService;
import vn.prostylee.useractivity.constant.TrackingType;
import vn.prostylee.useractivity.dto.filter.UserTrackingFilter;
import vn.prostylee.useractivity.service.UserTrackingService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SuggestionServiceImpl implements SuggestionService {

    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserTrackingService userTrackingService;
    private final AuthenticatedProvider authenticatedProvider;

    @Override
    public List<String> getHintKeywords(SuggestionKeywordFilter suggestionKeywordFilter) {
        if (StringUtils.isBlank(suggestionKeywordFilter.getKeyword())) {
            return Collections.emptyList();
        }
        Optional<TrackingType> trackingType = TrackingType.find(suggestionKeywordFilter.getType());
        if (trackingType.isPresent()) {
            Sort sort = Sort.by("name");
            Pageable pageable = PageRequest.of(suggestionKeywordFilter.getPage(), suggestionKeywordFilter.getLimit(), sort);
            switch (trackingType.get()) {
                case STORE:
                    return storeRepository.getStoreNamesLike(DbUtil.buildSearchLikeQuery(suggestionKeywordFilter.getKeyword()), pageable);
                case PRODUCT:
                    return productRepository.getProductNamesLike(DbUtil.buildSearchLikeQuery(suggestionKeywordFilter.getKeyword()), pageable);
                default:
                    break;
            }
        }
        return Collections.emptyList();
    }

    // TODO should be cached
    @Override
    public List<String> getTopKeywords(SuggestionKeywordFilter suggestionKeywordFilter) {
        TrackingType trackingType = TrackingType.find(suggestionKeywordFilter.getType()).orElse(null);
        UserTrackingFilter userTrackingFilter = UserTrackingFilter.builder()
                .trackingType(trackingType)
                .build();
        userTrackingFilter.setPage(suggestionKeywordFilter.getPage());
        userTrackingFilter.setLimit(suggestionKeywordFilter.getLimit());

        return userTrackingService.getTopKeywordsBy(userTrackingFilter);
    }

    @Override
    public List<String> getRecentKeywordsByMe(SuggestionKeywordFilter suggestionKeywordFilter) {
        return TrackingType.find(suggestionKeywordFilter.getType())
                .map(trackingType -> {
                    UserTrackingFilter userTrackingFilter = UserTrackingFilter.builder()
                            .trackingType(trackingType)
                            .userId(authenticatedProvider.getUserIdValue())
                            .build();
                    userTrackingFilter.setPage(suggestionKeywordFilter.getPage());
                    userTrackingFilter.setLimit(suggestionKeywordFilter.getLimit());

                    return userTrackingService.getRecentKeywordsBy(userTrackingFilter);
                })
                .orElseGet(Collections::emptyList);
    }
}
