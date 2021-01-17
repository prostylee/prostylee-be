package vn.prostylee.store.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.request.CompanyRequest;
import vn.prostylee.store.dto.response.CompanyResponse;

public interface CompanyService extends CrudService<CompanyRequest, CompanyResponse, Long> {
}
