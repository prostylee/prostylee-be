package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.entity.ProductShippingProvider;
import vn.prostylee.product.repository.ProductShippingProviderRepository;
import vn.prostylee.product.service.ProductShippingProviderService;

@Service
@RequiredArgsConstructor
public class ProductShippingProviderImpl implements ProductShippingProviderService {

    private final ProductShippingProviderRepository productShippingProviderRepository;
    @Override
    public boolean save(Long productId, Long shippingProviderId) {
        ProductShippingProvider entity =  new ProductShippingProvider(productId, shippingProviderId);
        productShippingProviderRepository.save(entity);
        return true;
    }
}
