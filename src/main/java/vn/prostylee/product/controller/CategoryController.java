package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.controller.TrackingCrudController;
import vn.prostylee.product.dto.filter.AttributeFilter;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.service.AttributeService;
import vn.prostylee.product.service.CategoryService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/categories")
public class CategoryController extends TrackingCrudController<CategoryRequest, CategoryResponse, Long, CategoryFilter> {

    private final AttributeService attributeService;

    private final CategoryService categoryService;
    @Autowired
    public CategoryController(CategoryService categoryService, AttributeService attributeService) {
        super(categoryService);
        this.attributeService = attributeService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}/attributes")
    public Page<AttributeResponse> getAttributes(@PathVariable("id") Long id) {
        AttributeFilter attributeFilter = new AttributeFilter();
        attributeFilter.setCategoryId(id);
       return this.attributeService.findAll(attributeFilter);
    }

    @GetMapping("/{id}/attributes/{attributeId}")
    public AttributeResponse getAttribute(@PathVariable("id") Long categoryId, @PathVariable("attributeId") Long attributeId) {
        return this.attributeService.findByIdCategoryIdAndId(categoryId, attributeId);
    }
}
