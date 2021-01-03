package vn.prostylee.useractivity.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;

public interface UserRatingService extends CrudService<UserRatingRequest, UserRatingResponse, Long> {
    /**
     * Count rating base on filter
     * @param filter
     * @return
     */
    long count(UserRatingFilter filter);

    /**
     * Get average base on filter
     * @param filter
     * @return
     */
    double average(UserRatingFilter filter);
}
