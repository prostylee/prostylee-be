package vn.prostylee.product.service;

import vn.prostylee.product.dto.response.ProductStatisticResponse;
import vn.prostylee.product.entity.Product;

public interface ProductStatisticService{

    ProductStatisticResponse findById(Long id);
}
