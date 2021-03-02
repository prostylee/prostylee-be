package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;

public interface AttributeService extends CrudService<AttributeRequest, AttributeResponse, Long> {

    AttributeResponse findByIdCategoryIdAndId(Long categoryId, Long id);

}
