package vn.prostylee.product.service;

import org.springframework.data.domain.Page;
import vn.prostylee.order.dto.filter.BestSellerFilter;
import vn.prostylee.product.dto.filter.RelatedProductFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;

public interface ProductCollectionService {

    Page<ProductResponse> getRelatedProducts(Long productId, RelatedProductFilter relatedProductFilter);

    Page<ProductResponse> getSuggestionProducts(Long productId, SuggestionProductFilter suggestionProductFilter);

    Page<ProductResponse> getBestSellerProducts(BestSellerFilter bestSellerFilter);
}
