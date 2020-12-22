package vn.prostylee.media.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.util.StringUtils;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.core.utils.BeanUtil;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachement;
import vn.prostylee.media.exception.FileUploaderException;
import vn.prostylee.media.repository.AttachementRepository;
import vn.prostylee.media.service.FileUploadService;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Image Service
 */
@Service
public class AwsS3ImageServiceImpl implements FileUploadService {

    private static final String FILE_URL_PREFIX_FORMAT = "https://##BUCKET_NAME##.s3-##REGION##.amazonaws.com/";
    private static final String BUCKET_NAME_KEY = "##BUCKET_NAME##";
    private static final String REGION_KEY = "##REGION##";
    private static final String EMPTY_STRING = "";
    private static final String SEPARATOR = "/";
    private static final String FILE_UPLOAD_ERROR = "Uploading file to S3 bucket was failed";
    private static final String FILE_DELETE_ERROR = "Deleting file from S3 bucket was failed";
    private static final String INVALID_FILE_URL = "Invalid file url";
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String fileUrlPrefix;
    private final AttachementRepository attachmentRepository;

    @Autowired
    public AwsS3ImageServiceImpl(@Value("${app.aws.bucket}") String bucketName,
                                 @Value("${app.aws.region}") String region,
                                 @Value("${app.aws.accessKey}") String accessKey,
                                 @Value("${app.aws.secretKey}") String secretKey,
                                 AttachementRepository attachmentRepository) {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = new AmazonS3Client(credentials);
        this.bucketName = bucketName;
        this.fileUrlPrefix = FILE_URL_PREFIX_FORMAT
                .replace(BUCKET_NAME_KEY, bucketName)
                .replace(REGION_KEY, region);
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

    @Override
    public List<AttachmentResponse> uploadFiles(MultipartFile... files) {
        return uploadFiles("", files);
    }

    @Override
    public List<AttachmentResponse> uploadFiles(String folderId, MultipartFile... files) {
        List<AttachmentResponse> attachments = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (StringUtils.isEmpty(file.getOriginalFilename())) {
                    continue;
                }
                String folder = StringUtils.isEmpty(folderId) ? "" : folderId + SEPARATOR;
                String fileName = folder + generateUniqueName();
                s3Client.putObject(bucketName, fileName, file.getInputStream(), getMetaData(file));
                URL storedUrl = s3Client.getUrl(bucketName, fileName);
                Attachement attachment = saveUploadFiles(storedUrl, file);
                AttachmentResponse attachmentDto = BeanUtil.copyProperties(attachment, AttachmentResponse.class);
                attachments.add(attachmentDto);
            }
            return attachments;
        } catch (AmazonClientException | IOException e) {
            throw new FileUploaderException(FILE_UPLOAD_ERROR, e);
        }
    }

    @Override
    public boolean deleteFiles(String... fileIds) {
        try {
            for(String fileId : fileIds) {
                long formattedFileId = Long.valueOf(fileId);
                final Attachement attachement = getAttachement(formattedFileId);
                attachmentRepository.deleteById(formattedFileId);
                String fileName = attachement.getName();
                s3Client.deleteObject(bucketName, fileName);
            }
            return true;
        } catch (IllegalArgumentException | AmazonClientException e) {
            throw new FileUploaderException(FILE_DELETE_ERROR, e);
        }
    }

    private Attachement getAttachement(long fileId) {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment is not exists by getting with id " + fileId));
    }

    private Attachement saveUploadFiles(URL fileUrl, MultipartFile file) {
        if (fileUrl == null) {
            return null;
        }
        Attachement attachment = new Attachement();
        attachment.setType(file.getContentType());
        attachment.setPath(fileUrl.toString());
        attachment.setName(fileUrl.getFile().replaceAll("/", ""));
        attachment.setDisplayName(file.getOriginalFilename());
        attachment.setSizeInKb(file.getSize() / 1024);
        return attachmentRepository.save(attachment);
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
}
