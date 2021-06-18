package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.product.converter.ProductAttributeConverter;
import vn.prostylee.product.dto.request.ProductAttributeRequest;
import vn.prostylee.product.dto.response.ProductAttributeResponse;
import vn.prostylee.product.entity.ProductAttribute;
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
    public Page<ProductAttributeResponse> findAll(BaseFilter baseFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductAttributeResponse findById(Long id) {
        return productAttributeConverter.toDto(this.getProductAttributeById(id));
    }

    @Override
    public ProductAttributeResponse save(ProductAttributeRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductAttributeResponse update(Long id, ProductAttributeRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.productAttributeRepository.deleteById(id);
            log.info("Product attribute with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Product attribute id {} does not exists", id);
            throw new ResourceNotFoundException("Product attribute is not found with id [" + id + "]");
        }
    }

    public ProductAttribute getProductAttributeById(Long id) {
        return productAttributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product attribute is not found with id [" + id + "]"));
    }

    @Override
    public List<ProductAttribute> getProductAttributeByProductId(Long productId) {
        return productAttributeRepository.getProductAttributeByProductId(productId);
    }

    @Override
    public List<ProductAttribute> getProductAttributeByPriceId(Long priceId) {
        return productAttributeRepository.getProductAttributeByPriceId(priceId);
    }

    @Override
    public List<ProductAttribute> getProductAttributeByIds(List<Long> ids) {
        return productAttributeRepository.findByIdIn(ids);
    }
}
