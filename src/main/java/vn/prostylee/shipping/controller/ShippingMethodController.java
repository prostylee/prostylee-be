package vn.prostylee.shipping.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.shipping.dto.filter.ShippingMethodFilter;
import vn.prostylee.shipping.dto.request.ShippingMethodRequest;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;
import vn.prostylee.shipping.service.ShippingMethodService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/shippings/methods")
public class ShippingMethodController extends CrudController<ShippingMethodRequest, ShippingMethodResponse, Long, ShippingMethodFilter> {

    public ShippingMethodController(ShippingMethodService service) {
        super(service);
    }
}
