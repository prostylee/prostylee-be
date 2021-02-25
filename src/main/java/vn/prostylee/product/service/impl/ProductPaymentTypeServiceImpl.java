package vn.prostylee.product.service.impl;

import org.springframework.stereotype.Service;
import vn.prostylee.product.service.ProductPaymentTypeService;

@Service
public class ProductPaymentTypeServiceImpl implements ProductPaymentTypeService {
    @Override
    public boolean save(Long productId, Long paymentTypeId) {
        return false;
    }
}
