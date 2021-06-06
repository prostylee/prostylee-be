package vn.prostylee.shipping.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.shipping.dto.filter.ShippingProviderFilter;
import vn.prostylee.shipping.dto.request.ShippingProviderRequest;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.service.ShippingProviderService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/shippings/providers")
public class ShippingProviderController extends CrudController<ShippingProviderRequest, ShippingProviderResponse, Long, ShippingProviderFilter> {

    public ShippingProviderController(ShippingProviderService service) {
        super(service);
    }
}
