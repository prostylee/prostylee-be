package vn.prostylee.shipping.service;

import vn.prostylee.core.service.MasterDataService;
import vn.prostylee.shipping.dto.response.ShippingAddressResponse;
import vn.prostylee.shipping.entity.ShippingAddress;

public interface ShippingAddressService extends MasterDataService<ShippingAddressResponse> {
    ShippingAddress getShippingAddressById(Long id);
}
