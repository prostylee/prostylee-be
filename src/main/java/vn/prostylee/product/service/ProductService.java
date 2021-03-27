package vn.prostylee.product.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.filter.RelatedProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Product;

public interface ProductService extends CrudService<ProductRequest, ProductResponse, Long> {

    long countTotalProductByUser(Long userId);

    Product getProductById(Long id);

    Page<ProductResponse> getRelatedProducts(Long productId, RelatedProductFilter relatedProductFilter);
}
