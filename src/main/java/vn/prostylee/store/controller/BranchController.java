package vn.prostylee.store.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.controller.CrudController;
import vn.prostylee.store.dto.filter.BranchFilter;
import vn.prostylee.store.dto.request.BranchRequest;
import vn.prostylee.store.dto.response.CitiesWithBranchesResponse;
import vn.prostylee.store.dto.response.BranchResponse;
import vn.prostylee.store.service.BranchService;

import java.util.List;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/branches")
public class BranchController extends CrudController<BranchRequest, BranchResponse, Long, BranchFilter> {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        super(branchService);
        this.branchService = branchService;
    }

    @GetMapping("/cities/{storeId}")
    public List<CitiesWithBranchesResponse> getListCities(@PathVariable(value = "storeId") Long storeId){
        return branchService.getListCities(storeId);
    }
}
