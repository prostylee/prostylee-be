package vn.prostylee.app.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.app.dto.response.AppMasterDataResponse;
import vn.prostylee.app.service.AppMetadataService;
import vn.prostylee.core.constant.ApiVersion;

@AllArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/masters")
public class MasterDataController {

    private final AppMetadataService metadataService;

    @GetMapping("/app")
    public AppMasterDataResponse getAllMasterData() {
        AppMasterDataResponse response = new AppMasterDataResponse();
        response.setConfiguration(metadataService.getConfiguration());
        return response;
    }
}
