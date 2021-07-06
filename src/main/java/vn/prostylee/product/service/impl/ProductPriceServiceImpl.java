package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.converter.ProductPriceConverter;
import vn.prostylee.product.dto.request.ProductPriceRequest;
import vn.prostylee.product.dto.response.ProductPriceRangeResponse;
import vn.prostylee.product.dto.response.ProductPriceResponse;
import vn.prostylee.product.entity.ProductPrice;
import vn.prostylee.product.repository.ProductPriceRepository;
import vn.prostylee.product.service.ProductPriceService;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class ProductPriceServiceImpl implements ProductPriceService {

    private final ProductPriceRepository productPriceRepository;

    private final ProductPriceConverter productPriceConverter;

    @Override
    public Page<ProductPriceResponse> findAll(BaseFilter baseFilter) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ProductPriceResponse findById(Long id) {
        return productPriceConverter.toDto(this.getProductPriceById(id));
    }

    @Override
    public ProductPriceResponse save(ProductPriceRequest request) {
        ProductPrice productPrice = BeanUtil.copyProperties(request, ProductPrice.class);
        productPriceConverter.toEntity(request, productPrice);
        return productPriceConverter.toDto(this.productPriceRepository.save(productPrice));
    }

    @Override
    public ProductPriceResponse update(Long id, ProductPriceRequest request) {
        ProductPrice productPrice = this.getProductPriceById(id);
        BeanUtil.mergeProperties(request, productPrice);
        productPriceConverter.toEntity(request, productPrice);
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

    @Override
    public ProductPrice getProductPriceById(Long id) {
        return productPriceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product price is not found with id [" + id + "]"));
    }

    @Override
    public List<ProductPrice> getProductPricesByProduct(Long productId) {
        return productPriceRepository.findAllByProductId(productId);
    }

    @Override
    public ProductPriceRangeResponse getProductPriceRange(){
        Double minPrice = productPriceRepository.getMinProductPrice();
        Double maxPrice = productPriceRepository.getMaxProductPrice();
        ProductPriceRangeResponse productPriceRangeResponse = new ProductPriceRangeResponse();
        productPriceRangeResponse.setMinPrice(minPrice);
        productPriceRangeResponse.setMaxPrice(maxPrice);
        return productPriceRangeResponse;
    }
}
