package vn.prostylee.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.StoreFilter;
import vn.prostylee.store.dto.request.StoreRequest;
import vn.prostylee.store.dto.response.StoreResponse;
import vn.prostylee.store.service.StoreService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/stores")
public class StoreController extends CrudController<StoreRequest, StoreResponse, Long, StoreFilter> {

    public StoreController(StoreService storeService) {
        super(storeService);
    }
}
