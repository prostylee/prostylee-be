package vn.prostylee.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.product.entity.ProductPaymentType;
import vn.prostylee.product.repository.ProductPaymentTypeRepository;
import vn.prostylee.product.service.ProductPaymentTypeService;

@Service
@RequiredArgsConstructor
public class ProductPaymentTypeServiceImpl implements ProductPaymentTypeService {
    private final ProductPaymentTypeRepository productPaymentTypeRepository;

    @Override
    public boolean save(Long productId, Long paymentTypeId) {
        ProductPaymentType entity = new ProductPaymentType(productId, paymentTypeId);
        productPaymentTypeRepository.save(entity);
        return true;
    }
}
