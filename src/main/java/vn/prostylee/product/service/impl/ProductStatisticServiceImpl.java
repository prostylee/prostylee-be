package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.response.ProductStatisticResponse;
import vn.prostylee.product.entity.ProductStatistic;
import vn.prostylee.product.repository.ProductStatisticRepository;
import vn.prostylee.product.service.ProductStatisticService;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductStatisticServiceImpl implements ProductStatisticService {

    private final ProductStatisticRepository productStatisticRepository;

    @Override
    public ProductStatisticResponse findById(Long id){
        return BeanUtil.copyProperties(this.getById(id), ProductStatisticResponse.class);
    }

    @Override
    public Optional<ProductStatisticResponse> fetchById(Long id) {
        return productStatisticRepository.findById(id)
                .map(productStatistic -> BeanUtil.copyProperties(productStatistic, ProductStatisticResponse.class));
    }

    private ProductStatistic getById(Long id){
        return productStatisticRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product statistic is not found with id [" + id + "]"));
    }
}
