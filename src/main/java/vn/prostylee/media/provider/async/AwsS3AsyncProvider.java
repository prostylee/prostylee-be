package vn.prostylee.media.provider.async;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.repository.AttachmentRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

/**
 * The helper class for communicating files with AWS S3 in async mode
 */
@Component
@Slf4j
public class AwsS3AsyncProvider extends BaseAsyncProvider {

    private final AmazonS3 s3Client;
    private final String bucketName;
    private final AttachmentRepository attachmentRepository;

    @Autowired
    public AwsS3AsyncProvider(
            AmazonS3 s3Client,
            @Value("${app.aws.bucket}") String bucketName,
            AttachmentRepository attachmentRepository) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.attachmentRepository = attachmentRepository;
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
     * @param folderId The Google folder id will contains the uploaded file
     * @param file     The {@link MultipartFile}
     * @return The {@link AttachmentResponse}
     */
    @Async
    public Future<AttachmentResponse> uploadFile(String folderId, MultipartFile file) throws IOException {
        String folder = StringUtils.isEmpty(folderId) ? "" : folderId + AppConstant.PATH_SEPARATOR;
        String fileName = folder + generateUniqueName();
        s3Client.putObject(bucketName, fileName, file.getInputStream(), getMetaData(file));
        URL storedUrl = s3Client.getUrl(bucketName, fileName);
        Attachment attachment = saveUploadFiles(storedUrl, file);
        AttachmentResponse attachmentDto = BeanUtil.copyProperties(attachment, AttachmentResponse.class);
        return new AsyncResult<>(attachmentDto);
    }

    /**
     * Delete file from AWS S3
     *
     * @param fileNames The AWS S3 file names
     * @return True if delete success, otherwise false
     */
    @Async
    public Future<Boolean> deleteFiles(List<String> fileNames) {
        List<KeyVersion> keys = new ArrayList<>();
        for (String fileName : fileNames) {
            keys.add(new KeyVersion(fileName));
        }
        DeleteObjectsRequest requests = new DeleteObjectsRequest(bucketName).withKeys(keys);
        DeleteObjectsResult results = s3Client.deleteObjects(requests);
        return new AsyncResult<>(results.getDeletedObjects().size() == fileNames.size());
    }

    /**
     * This method generates meta data for the given file.
     *
     * @param file file
     * @return metadata
     */
    private ObjectMetadata getMetaData(MultipartFile file) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        return objectMetadata;
    }

    private Attachment saveUploadFiles(URL fileUrl, MultipartFile file) {
        if (fileUrl == null) {
            return null;
        }
        Attachment attachment = new Attachment();
        attachment.setType(file.getContentType());
        attachment.setPath(fileUrl.toString());
        attachment.setName(fileUrl.getFile().replaceAll("/", ""));
        attachment.setDisplayName(file.getOriginalFilename());
        attachment.setSizeInKb(file.getSize() / 1024);
        return attachmentRepository.save(attachment);
    }
}
