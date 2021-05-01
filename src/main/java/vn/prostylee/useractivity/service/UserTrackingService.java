package vn.prostylee.useractivity.service;

import vn.prostylee.useractivity.dto.filter.UserTrackingFilter;
import vn.prostylee.useractivity.dto.request.UserTrackingRequest;
import vn.prostylee.useractivity.dto.response.UserTrackingResponse;

import java.util.List;

public interface UserTrackingService {

    UserTrackingResponse storeTracking(UserTrackingRequest request);

    List<Long> getLastVisitedIdsBy(UserTrackingFilter userTrackingFilter);

    List<String> getRecentKeywordsBy(UserTrackingFilter userTrackingFilter);

    List<String> getTopKeywordsBy(UserTrackingFilter userTrackingFilter);

}
