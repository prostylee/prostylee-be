package vn.prostylee.location.controller;

import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.location.dto.filter.AddressFilter;
import vn.prostylee.location.dto.request.AddressRequest;
import vn.prostylee.location.dto.response.AddressResponse;
import vn.prostylee.location.service.AddressService;

@RestController
public class AddressController extends CrudController<AddressRequest, AddressResponse, Long, AddressFilter> {
    public AddressController(AddressService addressService) {
        super(addressService);
    }
}
