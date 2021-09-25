package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.AttributeRequest;
import vn.prostylee.product.dto.response.AttributeResponse;

import java.util.List;
import java.util.Set;

public interface AttributeService extends CrudService<AttributeRequest, AttributeResponse, Long> {

    List<AttributeResponse> findByIdIn(Set<Long> ids);
}
