package vn.prostylee.useractivity.service;

import org.springframework.data.jpa.repository.Query;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
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
    @Query(value = "SELECT avg(price) FROM Product")
    double average(UserRatingFilter filter);
}
