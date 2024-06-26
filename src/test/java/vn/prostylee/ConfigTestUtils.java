package vn.prostylee;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.configuration.AwsS3Properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfigTestUtils {

    public static final String FOLDER = "test-folder";
    public static final String BUCKET_NAME = "testaws";
    public static final String BUCKET_S3_FOLDER_NAME = "public";
    public static final String BUCKET_URL = "https://testaws.s3.ap-southeast-1.amazonaws.com/";
    public static final String CLOUDFRONT_URL = "https://d1fhfbd6s3w12p.cloudfront.net/";
    public static final String CLOUDFRONT_RESIZE_PREFIX = "fit-in/";

    private static final String IMAGE_PNG = "image/png";
    public static final MockMultipartFile mockMultipartFile =
            new MockMultipartFile("IMAGE_UPLOAD_PARAM_NAME", "test.png",IMAGE_PNG, "1234567890".getBytes());
    public static final List<MultipartFile> files = prepareFiles();
    private ConfigTestUtils(){};

    public static AwsS3Properties prepareAwsS3Props() {
        AwsS3Properties props = new AwsS3Properties();
        props.setBucket(BUCKET_NAME);
        props.setBucketUrl(BUCKET_URL);
        props.setCloudFrontUrl(CLOUDFRONT_URL);
        props.setS3MediaPublicFolder(BUCKET_S3_FOLDER_NAME);
        props.setS3ResizeImagePrefix(CLOUDFRONT_RESIZE_PREFIX);
        return props;
    }

    public static List<Long> prepareAttachmentsIds() {
        Long[] ids = new Long[] {1L,2L,3L};
        return Arrays.asList(ids);
    }

    private static List<MultipartFile> prepareFiles() {
        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile);
        return files;
    }
}
