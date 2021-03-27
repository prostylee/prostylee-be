package vn.prostylee.store.service;

import org.springframework.data.domain.Page;
import vn.prostylee.core.service.CrudService;
import vn.prostylee.store.dto.filter.MostActiveStoreFilter;
import vn.prostylee.store.dto.filter.StoreProductFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreMiniResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.dto.response.StoreResponseLite;

public interface StoreService extends CrudService<StoreRequest, StoreResponse, Long> {

    Page<StoreResponse> getTopProductsOfStores(MostActiveStoreFilter storeFilter);

    StoreResponseLite getStoreResponseLite(Long id);

    Page<StoreMiniResponse> getMiniStoreResponse(StoreProductFilter storeProductFilter);
}
