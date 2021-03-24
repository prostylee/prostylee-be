package vn.prostylee.location.controller;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.location.dto.AddressDto;
import vn.prostylee.location.dto.filter.AddressFilter;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.service.AddressService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/addresses")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/")
    public Page<AddressResponse> findAll(AddressFilter addressFilter) {
        return addressService.findAll(addressFilter);
    }

    @PostMapping("/imports")
    public AddressDto imports() {
        return this.addressService.imports();
    }
}
