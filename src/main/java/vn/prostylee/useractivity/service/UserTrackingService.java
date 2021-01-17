package vn.prostylee.useractivity.service;

import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.dto.response.UserTrackingResponse;

public interface UserTrackingService {

    UserTrackingResponse storeTracking(UserTrackingRequest request);

}
