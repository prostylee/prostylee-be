package vn.prostylee.store.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.StoreCategoryFilter;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreProductResponse;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/stores")
public class StoreController extends CrudController<StoreRequest, StoreResponse, Long, StoreFilter> {

    private StoreService storeService;

    public StoreController(StoreService storeService) {
        super(storeService);
        this.storeService = storeService;
    }

    @GetMapping("/top/products")
    public Page<StoreProductResponse> getTopStoreCategories(StoreCategoryFilter storeFilter) {
        return storeService.getTopStoreCategories(storeFilter);
    }
}
