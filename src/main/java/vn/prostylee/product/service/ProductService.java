package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.filter.RecentViewProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Product;

import java.util.List;

public interface ProductService extends CrudService<ProductRequest, ProductResponse, Long> {

    long countTotalProductByUser(Long userId);

    Product getProductById(Long id);

    List<ProductResponse> getRecentViewProducts(RecentViewProductFilter recentViewProductFilter);
}
