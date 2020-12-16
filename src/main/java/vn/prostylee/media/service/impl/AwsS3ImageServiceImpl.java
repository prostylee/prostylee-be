package vn.prostylee.media.service.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    private static final String IMAGE_URL_PREFIX_FORMAT = "https://##BUCKET_NAME##.s3.##REGION##.amazonaws.com/";
    private static final String BUCKET_NAME_KEY = "##BUCKET_NAME##";
    private static final String REGION_KEY = "##REGION##";
    private static final String EMPTY_STRING = "";
    private static final String SEPARATOR = "/";
    private static final String IMAGE_UPLOAD_ERROR = "Uploading image to S3 bucket was failed";
    private static final String IMAGE_DELETE_ERROR = "Deleting image from S3 bucket was failed";
    private static final String INVALID_IMAGE_URL = "Invalid image url";
    private final AmazonS3 s3Client;
    private final String bucketName;
    private final String imageUrlPrefix;
    private final AttachementRepository attachmentRepository;

    @Autowired
    public AwsS3ImageServiceImpl(AmazonS3 s3Client,
                                 @Value("${aws.bucket}") String bucketName,
                                 @Value("${aws.region}") String region,
                                 AttachementRepository attachmentRepository) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.imageUrlPrefix = IMAGE_URL_PREFIX_FORMAT
                .replace(BUCKET_NAME_KEY, bucketName)
                .replace(REGION_KEY, region);
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * This method generates unique image name.
     *
     * @return unique image name
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
                String fileName = folderId + SEPARATOR + generateUniqueName();
                s3Client.putObject(bucketName, fileName, file.getInputStream(), getMetaData(file));
                URL storedUrl = s3Client.getUrl(bucketName, fileName);
                Attachement attachment = saveUploadFiles(storedUrl, file);
                AttachmentResponse attachmentDto = BeanUtil.copyProperties(attachment, AttachmentResponse.class);
                attachments.add(attachmentDto);
            }
            return attachments;
        } catch (AmazonClientException | IOException e) {
            throw new FileUploaderException(IMAGE_UPLOAD_ERROR, e);
        }
    }

    @Override
    public boolean deleteFile(String fileId) {
        try {
            long formattedFileId = Long.valueOf(fileId);
            final Attachement attachement = getAttachement(formattedFileId);
            attachmentRepository.deleteById(formattedFileId);
            String fileName = getFileName(attachement.getName());
            s3Client.deleteObject(bucketName, fileName);
            return s3Client.doesObjectExist(bucketName, fileName);
        } catch (AmazonClientException e) {
            throw new FileUploaderException(IMAGE_DELETE_ERROR, e);
        }
    }

    private Attachement getAttachement(long fileId) {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not exists by getting with id " + fileId));
    }

    /**
     * This method returns image name from the url.
     *
     * @param imageUrl imageUrl
     * @return image name
     */
    private String getFileName(String imageUrl) {
        if (imageUrl.startsWith(imageUrlPrefix) && !imageUrl.replace(imageUrlPrefix, EMPTY_STRING).isEmpty()) {
            return imageUrl.replace(imageUrlPrefix, EMPTY_STRING).trim();
        } else {
            throw new FileUploaderException(INVALID_IMAGE_URL);
        }
    }

    private Attachement saveUploadFiles(URL fileUrl, MultipartFile file) {
        if (fileUrl == null) {
            return null;
        }
        Attachement attachment = new Attachement();
        attachment.setType(file.getContentType());
        attachment.setPath(fileUrl.toString());
        attachment.setName(fileUrl.getFile());
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
