package vn.prostylee.shipping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.core.dto.filter.MasterDataFilter;
import vn.prostylee.shipping.dto.request.ShippingAddressRequest;
import vn.prostylee.shipping.dto.response.ShippingProviderResponse;
import vn.prostylee.shipping.service.ShippingProviderService;

@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/shippings")
public class ShippingProviderController extends MasterDataController<ShippingProviderResponse> {

    private ShippingProviderService service;

    @Autowired
    public ShippingProviderController(ShippingProviderService service) {
        super(service);
        this.service = service;
    }

    @GetMapping("/providers")
    public Page<ShippingProviderResponse> getAll(MasterDataFilter filter){
        return super.getAll(filter);
    }

    @GetMapping("/providers-fee")
    public Page<ShippingProviderResponse> getProfiderAndFee(ShippingAddressRequest shippingAddressRequest){
        MasterDataFilter filter = new MasterDataFilter();
        return super.getAll(filter);
    }
}
