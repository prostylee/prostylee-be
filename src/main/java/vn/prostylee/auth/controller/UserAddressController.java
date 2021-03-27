package vn.prostylee.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.dto.filter.UserAddressFilter;
import vn.prostylee.auth.dto.request.UserAddressRequest;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.auth.service.UserAddressService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/user-addresses")
public class UserAddressController extends CrudController<UserAddressRequest, UserAddressResponse, Long, UserAddressFilter> {

    @Autowired
    public UserAddressController(UserAddressService userProfileService) {
        super(userProfileService);
    }
}
