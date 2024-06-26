package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.dto.response.CategoryResponseLite;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.CategoryRepository;
import vn.prostylee.product.repository.ProductRepository;
import vn.prostylee.product.service.CategoryService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final BaseFilterSpecs<Category> baseFilterSpecs;

    private final AttributeRepository attributeRepository;

    @Override
    public Page<CategoryResponse> findAll(BaseFilter baseFilter) {
        CategoryFilter categoryFilter = (CategoryFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(categoryFilter);
        Page<Category> page = this.categoryRepository.findAllActive(this.buildSearchable(categoryFilter), pageable);
        return page.map(this::toResponse);
    }

    @Override
    public CategoryResponse findById(Long id) {
        return this.toResponse(this.getById(id));
    }

    @Override
    public CategoryResponse save(CategoryRequest request) {
        Category category = BeanUtil.copyProperties(request, Category.class);
        if (category.getOrder() == null) {
            category.setOrder(1);
        }
        if (category.getParentId() == null) {
            category.setParentId(0L);
        }
        if(category.getHotStatus() == null){
            category.setHotStatus(false);
        }
        this.setAttributes(category, request.getAttributeIds());
        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = this.getById(id);
        BeanUtil.mergeProperties(request, category);
        category.setAttributes(this.getAttributes(request.getAttributeIds()));
        if (category.getOrder() == null) {
            category.setOrder(1);
        }
        return toResponse(this.categoryRepository.save(category));
    }

    private Set<Attribute> getAttributes(Set<Long> attributeIds) {
        return Optional.ofNullable(attributeIds)
                .orElseGet(HashSet::new)
                .stream().map(attributeRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            return this.categoryRepository.softDelete(id) > 0;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Category id {} does not exists", id);
            return false;
        }
    }

    private CategoryResponse toResponse(Category category) {
        return BeanUtil.copyProperties(category, CategoryResponse.class);
    }

    private void setAttributes(Category category, Set<Long> attributeIdsRequest) {
        if (category.getAttributes() == null) {
            category.setAttributes(new HashSet<>());
        }
        Set<Attribute> attributes = attributeRepository.findByIdIn(attributeIdsRequest);
        category.getAttributes().addAll(attributes);
    }

    private Category getById(Long id) {
        return this.categoryRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found with id [" + id + "]"));
    }

    private Specification<Category> buildSearchable(CategoryFilter categoryFilter) {
        Specification<Category> spec = this.baseFilterSpecs.search(categoryFilter);
        if (categoryFilter.getParentId() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("parentId"), categoryFilter.getParentId()));
        }
        if (categoryFilter.getHotStatus() != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("hotStatus"), categoryFilter.getHotStatus()));
        }
        return spec;
    }

    @Override
    public List<CategoryResponseLite> getCategoriesByStore(Long storeId) {
        List<Long> categories = productRepository.getProductCategoriesByStore(storeId);
        return this.categoryRepository.findAllByIdIn(categories).stream()
                .map(this::toResponseLite)
                .collect(Collectors.toList());
    }

    public CategoryResponseLite toResponseLite(Category category) {
        return BeanUtil.copyProperties(category, CategoryResponseLite.class);
    }
}
