package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.useractivity.dto.filter.UserWishListFilter;
import vn.prostylee.useractivity.dto.request.UserWishListRequest;
import vn.prostylee.useractivity.dto.response.UserWishListResponse;

public interface UserWishListService {

    boolean addToWishList(UserWishListRequest request);

    boolean removeFromWishList(Long id);

    Page<UserWishListResponse> findAll(UserWishListFilter filter);
}
