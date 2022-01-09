package vn.prostylee.product.service;


import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;
import vn.prostylee.product.dto.response.CategoryResponseLite;

import java.util.List;

public interface CategoryService extends CrudService<CategoryRequest, CategoryResponse, Long> {

    List<CategoryResponseLite> getCategoriesByStore(Long storeId);

}
