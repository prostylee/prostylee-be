package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.prostylee.product.converter.ProductAttributeConverter;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.repository.ProductAttributeRepository;
import vn.prostylee.product.service.ProductAttributeService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductAttributeServiceImpl implements ProductAttributeService {

    private final ProductAttributeRepository productAttributeRepository;

    private final ProductAttributeConverter productAttributeConverter;

    @Override
    public List<ProductAttributeResponse> findByProductId(Long productId) {
        List<ProductAttribute> productAttributes = productAttributeRepository.findAllByProduct(new Product(productId));
        return productAttributeConverter.toResponse(productAttributes);
    }

    @Override
    public List<ProductAttributeResponse> findByProductPriceId(Long productPriceId) {
        List<ProductAttribute> productAttributes = productAttributeRepository.findAllByProductPrice(new ProductPrice(productPriceId));
        return productAttributeConverter.toResponse(productAttributes);
    }

    @Override
    public List<ProductAttribute> findByIds(List<Long> ids) {
        return productAttributeRepository.findByIdIn(ids);
    }
}
