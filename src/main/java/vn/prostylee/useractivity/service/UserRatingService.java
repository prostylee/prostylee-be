package vn.prostylee.useractivity.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.useractivity.dto.request.UserRatingRequest;
import vn.prostylee.useractivity.dto.response.UserRatingResponse;

public interface UserRatingService extends CrudService<UserRatingRequest, UserRatingResponse, Long> {
}
