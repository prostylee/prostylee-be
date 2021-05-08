package vn.prostylee.product.repository;

import org.springframework.data.domain.Page;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.entity.Product;

import java.util.List;

public interface ProductExtRepository {

    Page<Product> findAll(ProductFilter productFilter);

    List<Product> getRandomProducts(SuggestionProductFilter suggestionProductFilter);
}
