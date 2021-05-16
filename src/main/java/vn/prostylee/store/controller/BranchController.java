package vn.prostylee.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.BranchFilter;
import vn.prostylee.store.dto.request.BranchRequest;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.service.BranchService;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/branches")
public class BranchController extends CrudController<BranchRequest, BranchResponse, Long, BranchFilter> {

    public BranchController(BranchService branchService) {
        super(branchService);
    }
}
