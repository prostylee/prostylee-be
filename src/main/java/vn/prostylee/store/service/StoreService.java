package vn.prostylee.store.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.filter.StoreCategoryFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreProductResponse;
import vn.prostylee.store.dto.response.StoreResponse;

public interface StoreService extends CrudService<StoreRequest, StoreResponse, Long> {

    Page<StoreProductResponse> getTopStoreCategories(StoreCategoryFilter storeCategoryFilter);
}
