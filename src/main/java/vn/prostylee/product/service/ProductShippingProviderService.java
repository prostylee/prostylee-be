package vn.prostylee.product.service;

public interface ProductShippingProviderService {
    boolean save(Long productId, Long shippingProviderId);
}
