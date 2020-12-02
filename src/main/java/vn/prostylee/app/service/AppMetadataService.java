package vn.prostylee.app.service;

import vn.prostylee.app.dto.AppConfiguration;

public interface AppMetadataService {

    boolean updateConfiguration(AppConfiguration configuration);

    AppConfiguration getConfiguration();
}
