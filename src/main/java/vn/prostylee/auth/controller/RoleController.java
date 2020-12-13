package vn.prostylee.auth.controller;

import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.MasterDataController;
import vn.prostylee.auth.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/roles")
public class RoleController extends MasterDataController<RoleResponse> {

    @Autowired
    public RoleController(RoleService service) {
        super(service);
    }
}
