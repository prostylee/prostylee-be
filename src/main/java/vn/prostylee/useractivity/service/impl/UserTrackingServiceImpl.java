package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.dto.filter.UserTrackingFilter;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.dto.response.UserTrackingResponse;
import vn.prostylee.useractivity.entity.UserTracking;
import vn.prostylee.useractivity.repository.UserTrackingRepository;
import vn.prostylee.useractivity.service.UserTrackingService;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserTrackingServiceImpl implements UserTrackingService {

    private final UserTrackingRepository repository;

    @Override
    public UserTrackingResponse storeTracking(UserTrackingRequest request) {
        UserTracking entity = BeanUtil.copyProperties(request, UserTracking.class);
        UserTracking savedEntity = repository.save(entity);
        return BeanUtil.copyProperties(savedEntity, UserTrackingResponse.class);
    }

    @Override
    public List<Long> getLastVisitedIdsBy(UserTrackingFilter userTrackingFilter) {
        Pageable pageable = PageRequest.of(userTrackingFilter.getPage(), userTrackingFilter.getLimit());

        switch (userTrackingFilter.getTrackingType()) {
            case STORE:
                return repository.getStoreIds(userTrackingFilter.getUserId(), pageable);
            case PRODUCT:
                return repository.getProductIds(userTrackingFilter.getUserId(), pageable);
            case CATEGORY:
                return repository.getCategoryIds(userTrackingFilter.getUserId(), pageable);
            default:
                break;
        }
        return Collections.emptyList();
    }
}
