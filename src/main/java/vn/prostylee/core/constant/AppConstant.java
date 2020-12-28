package vn.prostylee.core.constant;

public final class AppConstant {

    public static final String BASE_PACKAGE = "vn.prostylee";

    public static final String PATH_SEPARATOR = "/";

    // Constant for uploading/ downloading file
    public static final String GOOGLE_DRIVE_QUERY_PARAM_ID = "id";
    public static final int BATCH_SIZE_FLUSH = 100;
    public static final int WAIT_ASYNC_DONE_IN_MS = 100; // mili-seconds

    // Constant for zip file
    public static final int BUFFER_SIZE = 4096;
    public static final String CONSUME_ZIP = "application/zip";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";

    // Constant for AWS configuration
    public static final String IMAGE_URL_PREFIX_FORMAT = "https://##BUCKET_NAME##.s3.##REGION##.amazonaws.com/";
    public static final String BUCKET_NAME_KEY = "##BUCKET_NAME##";
    public static final String REGION_KEY = "##REGION##";

    private AppConstant() {
        super();
    }
}
