package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.converter.ProductPriceConverter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.repository.ProductPriceRepository;
import vn.prostylee.product.service.ProductPriceService;

@Service
@AllArgsConstructor
@Slf4j
public class ProductPriceServiceImpl implements ProductPriceService {

    private final BaseFilterSpecs<Product> baseFilterSpecs;

    private final ProductPriceRepository productPriceRepository;

    private final ProductPriceConverter productPriceConverter;

    @Override
    public Page<ProductPriceResponse> findAll(BaseFilter baseFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductPriceResponse findById(Long id) {
        return productPriceConverter.toDto(this.getById(id));
    }

    @Override
    public ProductPriceResponse save(ProductPriceRequest productPriceRequest) {
        ProductPrice productPrice = BeanUtil.copyProperties(productPriceRequest, ProductPrice.class);
        return productPriceConverter.toDto(this.productPriceRepository.save(productPrice));
    }

    @Override
    public ProductPriceResponse update(Long id, ProductPriceRequest productPriceRequest) {
        ProductPrice productPrice = this.getById(id);
        BeanUtil.mergeProperties(productPriceRequest, productPrice);
        return productPriceConverter.toDto(this.productPriceRepository.save(productPrice));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.productPriceRepository.deleteById(id);
            log.info("Product price with id [{}] deleted successfully", id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Product price id {} does not exists", id);
            throw new ResourceNotFoundException("Product price is not found with id [" + id + "]");
        }
    }

    private ProductPrice getById(Long id) {
        return this.productPriceRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price is not found with id [" + id + "]"));
    }
}
