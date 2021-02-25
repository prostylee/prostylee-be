package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductPaymentType;
import vn.prostylee.product.repository.ProductPaymentTypeRepository;
import vn.prostylee.product.service.ProductPaymentTypeService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductPaymentTypeServiceImpl implements ProductPaymentTypeService {

    @Override
    public Set<ProductPaymentType> buildProductPaymentTypes(List<Long> paymentTypes, Product productEntity) {
        return paymentTypes.stream().map(shippingProviderId -> this.buildProductPaymentType(shippingProviderId, productEntity))
                .collect(Collectors.toSet());
    }

    private ProductPaymentType buildProductPaymentType(Long productPaymentId, Product productEntity) {
        return ProductPaymentType.builder().paymentTypeId(productPaymentId).product(productEntity).build();
    }
}