package vn.prostylee.product.service;

import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductPaymentType;

import java.util.List;
import java.util.Set;

public interface ProductPaymentTypeService {
    Set<ProductPaymentType> buildProductPaymentTypes(List<Long> paymentTypes, Product productEntity);
}
