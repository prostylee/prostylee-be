package vn.prostylee.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.dto.filter.UserAddressFilter;
import vn.prostylee.auth.dto.request.UserAddressRequest;
import vn.prostylee.auth.dto.response.UserAddressResponse;
import vn.prostylee.auth.service.UserAddressService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.core.dto.filter.BaseFilter;

import java.util.List;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/user-addresses")
public class UserAddressController extends CrudController<UserAddressRequest, UserAddressResponse, Long, UserAddressFilter> {

    private final UserAddressService userAddressService;
    
    @Autowired
    public UserAddressController(UserAddressService userProfileService,
                                 UserAddressService userAddressService) {
        super(userProfileService);
        this.userAddressService = userAddressService;
    }

    @GetMapping("/userLogin")
    public Page<UserAddressResponse> getUserLoginAddress(BaseFilter baseFilter){
        return userAddressService.findByUserLogin();
    }
}
