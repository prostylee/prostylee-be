package vn.prostylee.business.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.business.dto.filter.MasterDataFilter;
import vn.prostylee.business.dto.response.MasterDataResponse;
import vn.prostylee.business.service.AppMetadataService;

@AllArgsConstructor
@RestController
@RequestMapping(ApiVersion.API_V1 + "/masters")
public class MasterDataController {

    private final AppMetadataService metadataService;

    @GetMapping
    public MasterDataResponse getAllMasterData(MasterDataFilter filter) {
        MasterDataResponse response = new MasterDataResponse();
        response.setConfiguration(metadataService.getConfiguration());
        return response;
    }
}
