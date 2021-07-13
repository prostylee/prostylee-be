package vn.prostylee.post.service;

import vn.prostylee.post.dto.response.PostStatisticResponse;

import java.util.Optional;

public interface PostStatisticService {

    PostStatisticResponse findById(Long id);

    Optional<PostStatisticResponse> fetchById(Long id);
}
