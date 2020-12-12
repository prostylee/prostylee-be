package vn.prostylee.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.auth.dto.filter.UserFilter;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/users")
public class UserController extends CrudController<UserRequest, UserResponse, Long, UserFilter> {

    @Autowired
    public UserController(UserService service) {
        super(service);
    }
}
