package vn.prostylee.product.repository;

import vn.prostylee.product.dto.filter.SuggestionProductFilter;
import vn.prostylee.product.entity.Product;

import java.util.List;

public interface ProductExtRepository {

    List<Product> getRandomProducts(SuggestionProductFilter suggestionProductFilter);
}
