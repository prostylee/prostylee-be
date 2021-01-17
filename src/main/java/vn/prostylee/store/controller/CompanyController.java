package vn.prostylee.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.CompanyFilter;
import vn.prostylee.store.dto.request.CompanyRequest;
import vn.prostylee.store.dto.response.CompanyResponse;
import vn.prostylee.store.service.CompanyService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/companies")
public class CompanyController extends CrudController<CompanyRequest, CompanyResponse, Long, CompanyFilter> {

    public CompanyController(CompanyService companyService) {
        super(companyService);
    }
}
