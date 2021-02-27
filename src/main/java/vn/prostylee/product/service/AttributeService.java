package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;
import vn.prostylee.product.entity.Attribute;

public interface AttributeService extends CrudService<AttributeRequest, AttributeResponse, Long> {
    Attribute getAttributeById(Long id);
}
