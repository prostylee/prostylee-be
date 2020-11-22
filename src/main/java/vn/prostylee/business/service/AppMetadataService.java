package vn.prostylee.business.service;

import vn.prostylee.business.dto.AppConfiguration;

public interface AppMetadataService {

    boolean updateConfiguration(AppConfiguration configuration);

    AppConfiguration getConfiguration();
}
