package vn.prostylee.auth.controller;

import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.auth.dto.filter.AccountFilter;
import vn.prostylee.auth.dto.request.AccountRequest;
import vn.prostylee.auth.dto.response.AccountResponse;
import vn.prostylee.auth.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/accounts")
public class AccountController extends CrudController<AccountRequest, AccountResponse, Long, AccountFilter> {

    @Autowired
    public AccountController(@Qualifier("accountService") AccountService service) {
        super(service);
    }
}
