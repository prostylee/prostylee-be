package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.product.dto.filter.CategoryFilter;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.service.CategoryService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/categories")
public class CategoryController extends CrudController<CategoryRequest, CategoryResponse, Long, CategoryFilter> {

    @Autowired
    public CategoryController(@Qualifier CategoryService categoryService) {
        super(categoryService);
    }
}
