package vn.prostylee.shipping.service;

import vn.prostylee.core.service.CrudService;
import vn.prostylee.shipping.dto.request.ShippingMethodRequest;
import vn.prostylee.shipping.dto.response.ShippingMethodResponse;

public interface ShippingMethodService extends CrudService<ShippingMethodRequest, ShippingMethodResponse, Long> {
}
