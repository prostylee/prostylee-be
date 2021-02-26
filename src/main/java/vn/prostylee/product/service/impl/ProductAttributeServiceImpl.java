package vn.prostylee.product.service.impl;

import org.springframework.data.domain.Page;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.ProductAttribute;
import vn.prostylee.product.service.ProductAttributeService;

public class ProductAttributeServiceImpl implements ProductAttributeService {
    @Override
    public Page<ProductAttributeResponse> findAll(BaseFilter baseFilter) {
        return null;
    }

    @Override
    public ProductAttributeResponse findById(Long aLong) {
        return null;
    }

    @Override
    public ProductAttributeResponse save(ProductAttributeRequest productAttributeRequest) {
        return null;
    }

    @Override
    public ProductAttributeResponse update(Long aLong, ProductAttributeRequest s) {
        return null;
    }

    @Override
    public boolean deleteById(Long aLong) {
        return false;
    }

    @Override
    public ProductAttributeResponse toDto(ProductAttribute productAttribute) {
        return BeanUtil.copyProperties(productAttribute, ProductAttributeResponse.class);
    }
}
