package vn.prostylee.useractivity.service;

import org.springframework.data.domain.Page;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.useractivity.constant.TargetType;
import vn.prostylee.useractivity.dto.request.UserFollowerRequest;
import vn.prostylee.useractivity.dto.response.UserFollowerResponse;

public interface UserFollowerService {

    long count(BaseFilter baseFilter, TargetType type, Long id);

    Page<UserFollowerResponse> findAll(BaseFilter baseFilter, TargetType type, Long id, Long userId);

    UserFollowerResponse follow(Long id, UserFollowerRequest request);

    boolean unfollow(Long id);
}
