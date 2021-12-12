package vn.prostylee.media.provider.async;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.configuration.AwsS3Properties;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.service.AttachmentService;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Future;

/**
 * The helper class for communicating files with AWS S3 in async mode
 */
@Component
@Slf4j
public class AwsS3AsyncProvider extends BaseAsyncProvider {

    private final S3Client s3Client;
    private final AttachmentService attachmentService;
    private final String bucketName;

    @Autowired
    public AwsS3AsyncProvider(
            S3Client s3Client,
            AwsS3Properties awss3Properties,
            AttachmentService attachmentService) {
        this.s3Client = s3Client;
        this.bucketName = awss3Properties.getBucket();
        this.attachmentService = attachmentService;
    }

    /**
     * This method generates unique file name.
     *
     * @return unique file name
     */
    private static String generateUniqueName() {
        return UUID.randomUUID().toString();
    }

    /**
     * Upload files to AWS S3 asynchronous
     *
     * @param folderName The AWS S3 folder id will contain the uploaded file
     * @param file     The {@link MultipartFile}
     * @return The {@link AttachmentResponse}
     */
    @Async
    public Future<AttachmentResponse> uploadFile(String folderName, MultipartFile file) throws IOException {
        String fileName = generateFileName(file, folderName);
        PutObjectRequest putOb = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .metadata(getMetaData(file))
                .build();

        PutObjectResponse response = s3Client.putObject(putOb, RequestBody.fromBytes(file.getBytes()));
        log.debug("Uploaded file = {}", response);

        GetUrlRequest request = GetUrlRequest.builder().bucket(bucketName ).key(fileName).build();
        URL storedUrl = s3Client.utilities().getUrl(request);

        Attachment attachment = attachmentService.saveAttachmentByUploadFile(storedUrl, file);
        AttachmentResponse attachmentDto = BeanUtil.copyProperties(attachment, AttachmentResponse.class);
        return new AsyncResult<>(attachmentDto);
    }

    private String generateFileName(MultipartFile file, String folderId) {
        String folder = StringUtils.isEmpty(folderId) ? "" : folderId + AppConstant.PATH_SEPARATOR;
        String fileExt = FilenameUtils.getExtension(file.getOriginalFilename());
        String extPath = StringUtils.isEmpty(fileExt) ? "" : FilenameUtils.EXTENSION_SEPARATOR_STR + fileExt;
        return folder + generateUniqueName() + extPath;
    }

    /**
     * Delete file from AWS S3
     *
     * @param fileNames The AWS S3 file names
     * @return True if delete success, otherwise false
     */
    @Async
    public Future<Boolean> deleteFiles(List<String> fileNames) {
        ArrayList<ObjectIdentifier> toDelete = new ArrayList<>();
        for (String fileName : fileNames) {
            toDelete.add(ObjectIdentifier.builder().key(fileName).build());
        }

        DeleteObjectsRequest dor = DeleteObjectsRequest.builder()
                .bucket(bucketName)
                .delete(Delete.builder().objects(toDelete).build())
                .build();

        DeleteObjectsResponse response = s3Client.deleteObjects(dor);
        return new AsyncResult<>(response.deleted().size() == fileNames.size());
    }

    /**
     * This method generates meta data for the given file.
     *
     * @param file file
     * @return metadata
     */
    private Map<String, String> getMetaData(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("version", "1.0.0");
        return metadata;
    }

}
