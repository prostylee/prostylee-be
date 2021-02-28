package vn.prostylee.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.product.dto.response.UsedStatusResponse;
import vn.prostylee.product.service.UsedStatusService;

import java.util.List;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/used_statuses")
public class UsedStatusController {
    private final UsedStatusService usedStatusService;

    @Autowired
    public UsedStatusController(UsedStatusService usedStatusService) {
        this.usedStatusService = usedStatusService;
    }

    @GetMapping()
    public List<UsedStatusResponse> getAllByStore() {
        return usedStatusService.getUsedStatuses();
    }

}
