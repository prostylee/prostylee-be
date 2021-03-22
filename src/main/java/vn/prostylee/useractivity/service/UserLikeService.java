package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserLikeFilter;
import vn.prostylee.useractivity.dto.request.MostActiveRequest;
import vn.prostylee.useractivity.dto.request.StatusLikeRequest;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.response.UserLikeResponse;

import java.util.List;

public interface UserLikeService {

    long count(UserLikeFilter baseFilter);

    Page<UserLikeResponse> findAll(UserLikeFilter filter);

    boolean like(UserLikeRequest request);

    boolean unlike(UserLikeRequest request);

    List<Long> loadStatusLikes(StatusLikeRequest request);

    List<Long> getTopBeLikes(MostActiveRequest request);
}
