package vn.prostylee.app.controller;

import vn.prostylee.app.dto.AppConfiguration;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.app.service.AppMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(ApiVersion.API_V1 + "/configurations")
public class ConfigurationController {

    private final AppMetadataService metadataService;

    @Autowired
    public ConfigurationController(AppMetadataService metadataService) {
        this.metadataService = metadataService;
    }

    @GetMapping
    public AppConfiguration getConfiguration() {
        return metadataService.getConfiguration();
    }

    @PutMapping
    public boolean getConfiguration(@RequestBody @Valid AppConfiguration configuration) {
        return metadataService.updateConfiguration(configuration);
    }
}
