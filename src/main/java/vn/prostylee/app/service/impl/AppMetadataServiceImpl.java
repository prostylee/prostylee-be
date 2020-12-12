package vn.prostylee.app.service.impl;

import vn.prostylee.app.dto.AppConfiguration;
import vn.prostylee.app.entity.AppMetadata;
import vn.prostylee.app.repository.AppMetadataRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vn.prostylee.app.service.AppMetadataService;
import vn.prostylee.core.utils.JsonUtils;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AppMetadataServiceImpl implements AppMetadataService {

    private static final String CONFIGURATION_CODE = "CONFIGURATION";

    private final AppMetadataRepository repository;

    @Override
    public boolean updateConfiguration(AppConfiguration configuration) {
        Optional<AppMetadata> metadata = repository.findByCode(CONFIGURATION_CODE);
        AppMetadata entity = metadata.orElseGet(AppMetadata::new);
        entity.setCode(CONFIGURATION_CODE);
        entity.setContent(JsonUtils.toJson(configuration));
        repository.save(entity);
        return true;
    }

    @Override
    public AppConfiguration getConfiguration() {
        Optional<AppMetadata> metadata = repository.findByCode(CONFIGURATION_CODE);
        if(metadata.isPresent()) {
            AppMetadata entity = metadata.get();
            return JsonUtils.fromJson(entity.getContent(), AppConfiguration.class);
        }
        return new AppConfiguration();
    }
}
