package vn.prostylee.product.repository;

import org.springframework.data.domain.Page;
import vn.prostylee.product.dto.filter.NewFeedsFilter;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.ProductIdFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.dto.response.NewFeedResponse;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Product;

import java.util.List;

public interface ProductExtRepository {

    Page<Product> findAll(ProductFilter productFilter);

    List<Product> getRandomProducts(SuggestionProductFilter suggestionProductFilter);

    Page<NewFeedResponse> findNewFeedsOfStore(NewFeedsFilter newFeedsFilter);

    Page<Long> getProductIds(ProductIdFilter productIdFilter);
}
