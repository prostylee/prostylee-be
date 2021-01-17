package vn.prostylee.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.shipping.service.ShippingMethodService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/shippings")
public class ShippingMethodController extends MasterDataController<ShippingMethodResponse> {
    private ShippingMethodService service;

    @Autowired
    public ShippingMethodController(ShippingMethodService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/methods")
    public Page<ShippingMethodResponse> getAll(MasterDataFilter filter){
        return super.getAll(filter);
    }
}