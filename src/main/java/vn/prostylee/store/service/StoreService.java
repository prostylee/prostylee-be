package vn.prostylee.store.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreResponse;

public interface StoreService extends CrudService<StoreRequest, StoreResponse, Long> {
}
