package vn.prostylee.product.service;

import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductShippingProvider;

import java.util.List;
import java.util.Set;

public interface ProductShippingProviderService {
    Set<ProductShippingProvider> buildProductShippingProviders(List<Long> shippingProviders, Product productEntity);
}
