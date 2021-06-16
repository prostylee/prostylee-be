package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductShippingProvider;
import vn.prostylee.product.service.ProductShippingProviderService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductShippingProviderImpl implements ProductShippingProviderService {

    @Override
    public Set<ProductShippingProvider> buildProductShippingProviders(List<Long> shippingProviders, Product productEntity) {
        return shippingProviders.stream().map(shippingProviderId -> this.buildProductShippingProvider(shippingProviderId,productEntity))
                .collect(Collectors.toSet());
    }

    private ProductShippingProvider buildProductShippingProvider(Long shippingProviderId, Product productEntity) {
        return ProductShippingProvider.builder().shippingProviderId(shippingProviderId).product(productEntity).build();
    }
}
