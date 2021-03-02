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
import vn.prostylee.core.utils.EntityUtils;
import vn.prostylee.product.dto.filter.AttributeFilter;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.AttributeOption;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.service.AttributeService;

import javax.persistence.criteria.Join;
import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    private final BaseFilterSpecs<Attribute> baseFilterSpecs;

    @Override
    public Page<AttributeResponse> findAll(BaseFilter baseFilter) {
        AttributeFilter categoryFilter = (AttributeFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(categoryFilter);
        Page<Attribute> page = this.attributeRepository.findAll(this.buildSearchable(categoryFilter), pageable);
        return page.map(this::toResponse);
    }

    @Override
    public AttributeResponse findById(Long id) {
        return this.toResponse(this.getById(id));
    }

    private Specification<Attribute> buildSearchable(AttributeFilter attributeFilter) {
        Specification<Attribute> spec = baseFilterSpecs.search(attributeFilter);
        if (attributeFilter.getCategoryId() != null) {
            Specification<Attribute> joinCategorySpec = (root, query, cb) -> {
                Join<Attribute, Category> category = root.join("category");
                return cb.equal(category.get("id"), attributeFilter.getCategoryId());
            };
            spec = spec.and(joinCategorySpec);
        }
        return spec;
    }

    @Override
    public AttributeResponse findByIdCategoryIdAndId(Long categoryId, Long id) {
        return this.toResponse(this.getByCategoryIdAndId(categoryId, id));
    }

    @Override
    public AttributeResponse save(AttributeRequest productRequest) {
        Attribute attribute = BeanUtil.copyProperties(productRequest, Attribute.class);
        attribute.setCategory(Category.builder().id(productRequest.getCategoryId()).build());
        if (attribute.getAttributeOptions() == null) {
            attribute.setAttributeOptions(new HashSet<>());
        }
        Set<AttributeOption> mergedAttributes = EntityUtils.merge(attribute.getAttributeOptions(), productRequest.getAttributeOptions(), "id", AttributeOption.class);
        mergedAttributes.forEach(attributeOption -> attributeOption.setAttribute(attribute));
        return toResponse(this.attributeRepository.save(attribute));
    }

    @Override
    public AttributeResponse update(Long id, AttributeRequest request) {
        Attribute attribute = this.getById(id);
        BeanUtil.mergeProperties(request, attribute);
        if (attribute.getOrder() == null) {
            attribute.setOrder(1);
        }
        Set<AttributeOption> mergedAttributes = EntityUtils.merge(attribute.getAttributeOptions(), request.getAttributeOptions(), "id", AttributeOption.class);
        mergedAttributes.forEach(attributeOption -> attributeOption.setAttribute(attribute));
        attribute.setAttributeOptions(mergedAttributes);
        return toResponse(this.attributeRepository.save(attribute));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.attributeRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Category id {} does not exists", id);
            return false;
        }
    }

    private Attribute getByCategoryIdAndId(Long categoryId, Long id) {
        return this.attributeRepository.findByCategoryIdAndId(categoryId, id)
                .orElseThrow(() -> new ResourceNotFoundException("Category or Attribute is not found with id [" + id + "]"));
    }

    private AttributeResponse toResponse(Attribute attribute) {
        AttributeResponse attributeResponse = BeanUtil.copyProperties(attribute, AttributeResponse.class);
        attributeResponse.setCategoryId(attribute.getCategory().getId());
        return attributeResponse;
    }

    private Attribute getById(Long id) {
        return this.attributeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attribute is not found with id [" + id + "]"));
    }

}
