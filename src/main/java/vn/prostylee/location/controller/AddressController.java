package vn.prostylee.location.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.filter.AddressFilter;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.service.AddressService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/addresses")
public class AddressController extends CrudController<AddressRequest, AddressResponse, Long, AddressFilter> {
    private final AddressService addressService;
    public AddressController(AddressService addressService, AddressService addressService1) {
        super(addressService);
        this.addressService = addressService1;
    }

    @PostMapping("/imports")
    public AddressDto imports() {
        return this.addressService.imports();
    }
}
