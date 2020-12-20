package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.filter.ProductFilter;
import vn.prostylee.product.dto.request.ProductRequest;
import vn.prostylee.product.dto.response.ProductResponse;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.entity.Product;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.ProductService;

import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final BaseFilterSpecs<Category> baseFilterSpecs;

    private final ProductRepository productRepository;

    @Override
    public Page<ProductResponse> findAll(BaseFilter baseFilter) {
        ProductFilter productFilter = (ProductFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(productFilter);
        Page<Product> page = this.productRepository.findAllActive(pageable);
        return page.map(this::toResponse);
    }

    @Override
    public ProductResponse findById(Long id) {
        return this.toResponse(this.getById(id));
    }

    @Override
    public ProductResponse save(ProductRequest productRequest) {
        Product product = BeanUtil.copyProperties(productRequest, Product.class);
        //product.setStatus(Product.Status.PUBLISHED);
        return toResponse(this.productRepository.save(product));
    }

    @Override
    public ProductResponse update(Long id, ProductRequest productRequest) {
        Product product = this.getById(id);
        if (Objects.nonNull(product)) {
            BeanUtil.mergeProperties(productRequest, product);
        }
        return toResponse(this.productRepository.save(product));
    }

    @Override
    public boolean deleteById(Long id) {
        return this.productRepository.softDelete(id) > 0 ? true : false;
    }

    @Override
    public boolean isEntityExists(Long aLong, Map<String, Object> uniqueValues) {
        return false;
    }

    @Override
    public boolean isFieldValueExists(String fieldName, Object value) {
        return false;
    }

    private ProductResponse toResponse(Product product) {
        return BeanUtil.copyProperties(product, ProductResponse.class);
    }

    private Product getById(Long id) {
        return this.productRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found with id [" + id + "]"));
    }
}
