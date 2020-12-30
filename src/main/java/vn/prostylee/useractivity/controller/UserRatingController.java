package vn.prostylee.useractivity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.useractivity.dto.filter.UserRatingFilter;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;
import vn.prostylee.useractivity.service.UserRatingService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/user-ratings")
public class UserRatingController extends CrudController<UserRatingRequest, UserRatingResponse, Long, UserRatingFilter> {

    private final UserRatingService userRatingService;

    @Autowired
    public UserRatingController(UserRatingService userRatingService) {
        super(userRatingService);
        this.userRatingService = userRatingService;
    }
}
