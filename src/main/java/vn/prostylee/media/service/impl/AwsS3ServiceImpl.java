package vn.prostylee.media.service.impl;

import com.amazonaws.AmazonClientException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.constant.ErrorResponseStatus;
import vn.prostylee.media.configuration.AWSS3Properties;
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

/**
 * AWS S3 File Upload Service
 */
@Service
public class AwsS3ServiceImpl implements FileUploadService {

    private final AwsS3AsyncProvider awsS3AsyncProvider;
    private final AttachmentRepository attachmentRepository;
    private final String bucketUrl;
    private final String cloudfrontUrl;

    @Autowired
    public AwsS3ServiceImpl(
            AWSS3Properties awss3Properties,
            AwsS3AsyncProvider awsS3AsyncProvider,
            AttachmentRepository attachmentRepository) {
        this.awsS3AsyncProvider = awsS3AsyncProvider;
        this.attachmentRepository = attachmentRepository;
        this.bucketUrl = awss3Properties.getBucketUrl();
        this.cloudfrontUrl = awss3Properties.getCloudFrontUrl();
    }

    @Override
    public List<String> getFiles(List<Long> fileIds, int width, int height) {
        List<Attachment> attachments = attachmentRepository.findAllById(fileIds);
        List<String> urls = new ArrayList<>();
        for(Attachment attachment : attachments) {
            urls.add(addSizeForFile(attachment.getPath(), width, height));
        }
        return urls;
    }

    private String addSizeForFile(String path, int width, int height) {
        String modifiedPath = path;
        if (width > 0 && height > 0) {
            modifiedPath = path.replace(bucketUrl,
                    cloudfrontUrl + width + "x" + height + AppConstant.PATH_SEPARATOR);
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
        if (CollectionUtils.isEmpty(files)) {
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
            throw new FileUploaderException(ErrorResponseStatus.FILE_UPLOAD_ERROR.getCode(), e);
        }
        return attachments;
    }

    @Override
    public boolean deleteFiles(List<Long> fileIds) {
        try {
            List<String> fileNames = new ArrayList<>();
            List<Attachment> attachments = attachmentRepository.findAllById(fileIds);
            for(Attachment attachment : attachments) {
                fileNames.add(attachment.getName());
            }
            attachmentRepository.deleteAttachmentsByIdIn(fileIds);
            awsS3AsyncProvider.deleteFiles(fileNames);
            return true;
        } catch (IllegalArgumentException | AmazonClientException e) {
            throw new FileUploaderException(ErrorResponseStatus.FILE_DELETE_ERROR.getCode(), e);
        }
    }

}
