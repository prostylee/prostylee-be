package vn.prostylee.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.service.ShippingProviderService;

@RestController("shippingProviderController")
@RequestMapping(value = ApiVersion.API_V1 + "/shippings")
public class ShippingProviderController extends MasterDataController<ShippingProviderResponse> {

    private ShippingProviderService service;

    @Autowired
    public ShippingProviderController(ShippingProviderService service) {
        super(service);
        this.service = service;
    }
}
