package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductForStoryResponse;
import vn.prostylee.product.dto.response.ProductResponse;

public interface ProductService extends CrudService<ProductRequest, ProductResponse, Long> {
    long countTotalProductByUser();
}
