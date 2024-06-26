package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.PagingParam;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.RatingResultCountResponse;
import vn.prostylee.useractivity.dto.response.ReviewCountResponse;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;
import vn.prostylee.useractivity.entity.UserRating;

import java.util.List;

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

    Page<RatingResultCountResponse> countRatingResult(PagingParam pagingParam);

    Page<ReviewCountResponse> countNumberReview(PagingParam pagingParam);

    List<UserRating> getListRatingByUser(Long userId, Long productId);
}