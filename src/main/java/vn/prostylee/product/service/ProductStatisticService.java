package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductStatisticResponse;

import java.util.Optional;

public interface ProductStatisticService{

    ProductStatisticResponse findById(Long id);

    Optional<ProductStatisticResponse> fetchById(Long id);
}
