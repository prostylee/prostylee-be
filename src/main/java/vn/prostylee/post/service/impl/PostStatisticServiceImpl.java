package vn.prostylee.post.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.post.dto.response.PostStatisticResponse;
import vn.prostylee.post.entity.PostStatistic;
import vn.prostylee.post.repository.PostStatisticRepository;
import vn.prostylee.post.service.PostStatisticService;
import vn.prostylee.product.dto.response.ProductStatisticResponse;
import vn.prostylee.product.entity.ProductStatistic;
import vn.prostylee.product.repository.ProductStatisticRepository;
import vn.prostylee.product.service.ProductStatisticService;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class PostStatisticServiceImpl implements PostStatisticService {

    private final PostStatisticRepository postStatisticRepository;

    @Override
    public PostStatisticResponse findById(Long id){
        return BeanUtil.copyProperties(this.getById(id), PostStatisticResponse.class);
    }

    @Override
    public Optional<PostStatisticResponse> fetchById(Long id) {
        return postStatisticRepository.findById(id)
                .map(productStatistic -> BeanUtil.copyProperties(productStatistic, PostStatisticResponse.class));
    }

    private PostStatistic getById(Long id){
        return postStatisticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post statistic is not found with id [" + id + "]"));
    }
}
