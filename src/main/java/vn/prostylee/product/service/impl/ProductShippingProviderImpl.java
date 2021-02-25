package vn.prostylee.product.service.impl;

import org.springframework.stereotype.Service;
import vn.prostylee.product.service.ProductShippingProviderService;

@Service
public class ProductShippingProviderImpl implements ProductShippingProviderService {

    @Override
    public boolean save(Long productId, Long shippingProviderId) {
        return false;
    }
}
