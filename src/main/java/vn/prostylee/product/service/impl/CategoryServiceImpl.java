package vn.prostylee.product.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.prostylee.core.dto.filter.BaseFilter;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.specs.BaseFilterSpecs;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.core.utils.EntityUtils;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.AttributeOptionRequest;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.entity.Attribute;
import vn.prostylee.product.entity.AttributeOption;
import vn.prostylee.product.entity.Category;
import vn.prostylee.product.repository.AttributeOptionRepository;
import vn.prostylee.product.repository.AttributeRepository;
import vn.prostylee.product.repository.CategoryRepository;
import vn.prostylee.product.service.CategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final AttributeRepository attributeRepository;

    private final AttributeOptionRepository attrOptionRepository;

    private final BaseFilterSpecs<Category> baseFilterSpecs;

    @Override
    public Page<CategoryResponse> findAll(BaseFilter baseFilter) {
        CategoryFilter categoryFilter = (CategoryFilter) baseFilter;
        Pageable pageable = baseFilterSpecs.page(categoryFilter);
        Page<Category> page = this.categoryRepository.findAllActive(pageable);
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
        this.setAttributes(category, request.getAttributeRequests());
        return toResponse(categoryRepository.saveAndFlush(category));
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = this.getById(id);
        BeanUtil.mergeProperties(request, category);
        if (category.getOrder() == null) {
            category.setOrder(1);
        }
        this.updateAttribute(category, request.getAttributeRequests());

        return toResponse(this.categoryRepository.save(category));
    }

    @Override
    public boolean deleteById(Long id) {
        try {
            this.categoryRepository.softDelete(id);
            return true;
        } catch (EmptyResultDataAccessException | ResourceNotFoundException e) {
            log.debug("Category id {} does not exists", id);
            return false;
        }
    }

    private CategoryResponse toResponse(Category category) {
        return BeanUtil.copyProperties(category, CategoryResponse.class);
    }

    private void setAttributes(Category category, Set<AttributeRequest> attributeRequests) {
        if (category.getAttributes() == null) {
            category.setAttributes(new HashSet<>());
        }
        Set<Attribute> mergedAttributes = EntityUtils.merge(category.getAttributes(), attributeRequests, "id", Attribute.class);
        mergedAttributes.forEach(attribute -> attribute.setCategory(category));
        category.setAttributes(mergedAttributes);

        // TODO impl logic for attribute options

//        if (CollectionUtils.isNotEmpty(attributeRequests)) {
//            Set<Attribute> attributes = new HashSet<>();
//            attributeRequests.forEach(item -> {
//                Attribute attribute = BeanUtil.copyProperties(item, Attribute.class);
//                attribute.setCategory(category);
//                this.setAttributeOptions(attribute, item.getAttributeOptionRequests());
//                attributes.add(attribute);
//            });
//            category.setAttributes(attributes);
//        }
    }

    private void setAttributeOptions(Attribute attribute, Set<AttributeOptionRequest> attributeOptionRequests) {
        if (CollectionUtils.isNotEmpty(attributeOptionRequests)) {
            Set<AttributeOption> attributeOptions = new HashSet<>();
            attributeOptionRequests.forEach(option -> {
                AttributeOption attributeOption = BeanUtil.copyProperties(option, AttributeOption.class);
                attributeOption.setAttribute(attribute);
                attributeOptions.add(attributeOption);
            });
            attribute.setAttributeOptions(attributeOptions);
        }
    }

    private void updateAttribute(Category category, Set<AttributeRequest> attributeRequests) {
        List<Long> attrRequestIds = this.getAttrRequestIds(attributeRequests);
        if (attrRequestIds.isEmpty()) {
            return;
        }
        List<Attribute> attributes = this.attributeRepository.findAllById(attrRequestIds);
        attributeRequests.forEach(item -> {
            if (attrRequestIds.contains(item.getId())) {
                attributes.forEach(attr -> {
                    if (attr.getId().equals(item.getId())) {
                        BeanUtil.mergeProperties(item, attr);
                        this.updateAttributeOptions(attr, item.getAttributeOptionRequests());
                    }
                });
                this.attributeRepository.saveAll(attributes);
            }
        });
        category.getAttributes().addAll(attributes);
    }

    private void updateAttributeOptions(Attribute attribute, Set<AttributeOptionRequest> attributeOptionRequest) {
        List<Long> attrOptionsRequestIds = this.getAttrOptionsRequestIds(attributeOptionRequest);
        if (attrOptionsRequestIds.isEmpty()) {
            return;
        }
        List<AttributeOption> attrOptions = this.attrOptionRepository.findAllById(attrOptionsRequestIds);
        attributeOptionRequest.forEach(option -> {
            if (attrOptionsRequestIds.contains(option.getId())) {
                attrOptions.forEach(attributeOption -> {
                    if (attributeOption.getId().equals(option.getId())) {
                        BeanUtil.mergeProperties(option, attributeOption);
                    }
                });
                this.attrOptionRepository.saveAll(attrOptions);
            }
        });
        attribute.getAttributeOptions().addAll(attrOptions);

    }

    private Category getById(Long id) {
        return this.categoryRepository.findOneActive(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category is not found with id [" + id + "]"));
    }

    private List<Long> getAttrOptionsRequestIds(Set<AttributeOptionRequest> attributeOptionRequests) {
        if (Objects.isNull(attributeOptionRequests)) {
            return Collections.emptyList();
        }
        return attributeOptionRequests.stream().map(AttributeOptionRequest::getId).collect(Collectors.toList());
    }

    private List<Long> getAttrRequestIds(Set<AttributeRequest> attributeRequests) {
        if (Objects.isNull(attributeRequests)) {
            return Collections.emptyList();
        }
        return attributeRequests.stream().map(AttributeRequest::getId).collect(Collectors.toList());
    }
}
