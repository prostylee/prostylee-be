package vn.prostylee.product.service;

import org.springframework.data.domain.Page;
import vn.prostylee.product.dto.filter.PurchasedProductFilter;
import vn.prostylee.product.dto.response.ProductResponse;

public interface ProductOrderService {

    Page<ProductResponse> getPurchasedProductsByMe(PurchasedProductFilter purchasedProductFilter);

    Page<ProductResponse> getPurchasedProductsByUserId(Long userId, PurchasedProductFilter purchasedProductFilter);
}
