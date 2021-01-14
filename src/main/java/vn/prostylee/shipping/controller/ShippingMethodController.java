package vn.prostylee.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.shipping.service.ShippingMethodService;

@RestController("shippingMethodController")
@RequestMapping(value = ApiVersion.API_V1 + "/shippings")
public class ShippingMethodController extends MasterDataController<ShippingMethodResponse> {
    private ShippingMethodService service;

    @Autowired
    public ShippingMethodController(ShippingMethodService service) {
        super(service);
        this.service = service;
    }
}
