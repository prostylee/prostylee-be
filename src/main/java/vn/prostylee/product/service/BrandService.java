package vn.prostylee.product.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.product.dto.request.BrandRequest;
import vn.prostylee.product.dto.response.BrandResponse;

public interface BrandService extends CrudService<BrandRequest, BrandResponse, Long> {
}
