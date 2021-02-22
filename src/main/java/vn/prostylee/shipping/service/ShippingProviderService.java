package vn.prostylee.shipping.service;

import vn.prostylee.core.service.MasterDataService;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.entity.ShippingProvider;

public interface ShippingProviderService extends MasterDataService<ShippingProviderResponse> {
    ShippingProvider getShippingProviderById(Long id);
}
