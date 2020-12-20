package vn.prostylee.product.service;


import vn.prostylee.core.service.CrudService;
import vn.prostylee.core.validator.EntityExists;
import vn.prostylee.core.validator.FieldValueExists;
import vn.prostylee.product.dto.request.CategoryRequest;
import vn.prostylee.product.dto.response.CategoryResponse;

public interface CategoryService extends CrudService<CategoryRequest, CategoryResponse, Long>, FieldValueExists, EntityExists<Long> { }
