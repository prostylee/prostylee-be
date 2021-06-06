package vn.prostylee.shipping.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.shipping.dto.filter.ShippingProviderFilter;
import vn.prostylee.shipping.dto.request.ShippingAddressRequest;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.service.ShippingProviderService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/shippings/providers-fee")
public class ShippingProviderCalculationController {

    private final ShippingProviderService service;

    @GetMapping("/providers-fee")
    public Page<ShippingProviderResponse> getProvidersAndFee(ShippingAddressRequest shippingAddressRequest){
        return service.findAll(new ShippingProviderFilter()); // TODO calculate fee
    }
}
