package vn.prostylee.store.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.MostActiveStoreFilter;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.filter.StoreProductFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreMiniResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/stores")
public class StoreController extends CrudController<StoreRequest, StoreResponse, Long, StoreFilter> {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        super(storeService);
        this.storeService = storeService;
    }

    @GetMapping("/top/products")
    public Page<StoreResponse> getTopProductsOfStores(MostActiveStoreFilter storeFilter) {
        return storeService.getTopProductsOfStores(storeFilter);
    }

    @GetMapping("/mini-stores")
    public Page<StoreMiniResponse> getMiniStoreResponse(StoreProductFilter storeProductFilter) {
        return storeService.getMiniStoreResponse(storeProductFilter);
    }
}
