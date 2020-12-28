package vn.prostylee.media.service.impl;

import com.amazonaws.AmazonClientException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.exception.FileUploaderException;
import vn.prostylee.media.provider.async.AwsS3AsyncProvider;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.FileUploadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * AWS S3 File Upload Service
 */
@Service
public class AwsS3ServiceImpl implements FileUploadService {
    private static final String FILE_UPLOAD_ERROR = "Uploading file to S3 bucket was failed";
    private static final String FILE_DELETE_ERROR = "Deleting file from S3 bucket was failed";
    private final AwsS3AsyncProvider awsS3AsyncProvider;
    private final AttachmentRepository attachmentRepository;
    private final String fileUrlPrefix;

    @Autowired
    public AwsS3ServiceImpl(
            @Value("${app.aws.bucket}") String bucketName,
            @Value("${app.aws.region}") String region,
            AwsS3AsyncProvider awsS3AsyncProvider,
            AttachmentRepository attachmentRepository) {
        this.awsS3AsyncProvider = awsS3AsyncProvider;
        this.attachmentRepository = attachmentRepository;
        this.fileUrlPrefix = AppConstant.IMAGE_URL_PREFIX_FORMAT
                .replace(AppConstant.BUCKET_NAME_KEY, bucketName)
                .replace(AppConstant.REGION_KEY, region);
    }

    @Override
    public List<String> getFiles(List<String> fileIds, int width, int height) {
        List<Long> fileIdsAsLong = fileIds.stream().map(Long::valueOf).collect(Collectors.toList());
        List<Attachment> attachments = attachmentRepository.findAllById(fileIdsAsLong);
        List<String> urls = new ArrayList<>();
        for(Attachment attachment : attachments) {
            urls.add(addSizeForFile(attachment.getPath(), width, height));
        }
        return urls;
    }

    private String addSizeForFile(String path, int width, int height) {
        String modifiedPath = path;
        if (width > 0 && height > 0) {
            modifiedPath = path.replace(fileUrlPrefix,
                    fileUrlPrefix + width + "x" + height + AppConstant.PATH_SEPARATOR);
        }
        return modifiedPath;
    }

    @Override
    public List<AttachmentResponse> uploadFiles(List<MultipartFile> files) {
        return uploadFiles("", files);
    }

    @Override
    public List<AttachmentResponse> uploadFiles(String folderId, List<MultipartFile> files) {
        List<AttachmentResponse> attachments = new ArrayList<>();
        if (files == null || files.size() < 1) {
            return attachments;
        }
        try {
            List<Future<AttachmentResponse>> futures = new ArrayList<>();
            // Execute async upload file
            for (MultipartFile file : files) {
                if (StringUtils.isEmpty(file.getOriginalFilename())) {
                    continue;
                }
                futures.add(awsS3AsyncProvider.uploadFile(folderId, file));
            }

            while (!awsS3AsyncProvider.isAllFutureDone(futures)) {
                // Wait until done
                // If all are not Done. Pause 100ms for next re-check
                Thread.sleep(AppConstant.WAIT_ASYNC_DONE_IN_MS);
            }

            for(Future<AttachmentResponse> future : futures) {
                attachments.add(future.get());
            }
        } catch (AmazonClientException | InterruptedException | ExecutionException | IOException e) {
            throw new FileUploaderException(FILE_UPLOAD_ERROR, e);
        }
        return attachments;
    }

    @Override
    public boolean deleteFiles(List<String> fileIds) {
        try {
            List<String> fileNames = new ArrayList<>();
            for(String fileId : fileIds) {
                final Attachment attachment = getAttachment(Long.valueOf(fileId));
                fileNames.add(attachment.getName());
            }
            List<Long> fileIdsAsLong = fileIds.stream().map(Long::valueOf).collect(Collectors.toList());
            attachmentRepository.deleteAttachmentsByIdIn(fileIdsAsLong);
            awsS3AsyncProvider.deleteFiles(fileNames);
            return true;
        } catch (IllegalArgumentException | AmazonClientException e) {
            throw new FileUploaderException(FILE_DELETE_ERROR, e);
        }
    }

    private Attachment getAttachment(Long fileId) {
        return attachmentRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment is not exists by getting with id " + fileId));
    }

}
