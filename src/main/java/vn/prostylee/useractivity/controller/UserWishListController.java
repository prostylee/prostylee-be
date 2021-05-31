package vn.prostylee.useractivity.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;
import vn.prostylee.useractivity.dto.filter.UserWishListFilter;
import vn.prostylee.useractivity.dto.request.UserLikeRequest;
import vn.prostylee.useractivity.dto.request.UserWishListRequest;
import vn.prostylee.useractivity.dto.response.UserWishListResponse;
import vn.prostylee.useractivity.service.UserWishListService;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping(value = ApiVersion.API_V1 + "/user-wish-lists")
public class UserWishListController {

    private final UserWishListService userWishListService;

    @GetMapping
    public Page<UserWishListResponse> findAll(UserWishListFilter filter) {
        return userWishListService.findAll(filter);
    }


    @PostMapping
    public SimpleResponse addToWishList(@Valid @RequestBody UserWishListRequest request) {
        return SimpleResponse.builder().data(userWishListService.addToWishList(request)).build();
    }

    @DeleteMapping("/{id}")
    public SimpleResponse removeFromWishList(@PathVariable Long id) {
        return SimpleResponse.builder().data(userWishListService.removeFromWishList(id)).build();
    }

}
