package vn.prostylee.useractivity.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.dto.response.UserTrackingResponse;
import vn.prostylee.useractivity.entity.UserTracking;
import vn.prostylee.useractivity.repository.UserTrackingRepository;
import vn.prostylee.useractivity.service.UserTrackingService;

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
}
