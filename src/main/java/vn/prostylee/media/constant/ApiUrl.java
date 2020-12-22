package vn.prostylee.media.constant;

import vn.prostylee.core.constant.ApiVersion;

public final class ApiUrl {

    public static final String FILE_STORAGE = ApiVersion.API_V1 + "/file-storages";

    public static final String CLOUD_STORAGE = ApiVersion.API_V1 + "/cloud-storages";

    public static final String FILE_UPLOADER = ApiVersion.API_V1 + "/file-uploader";

    public static final String FILE_STORAGE_ACTION = "/{fileName:.+}";

    private ApiUrl() {
        super();
    }
}
