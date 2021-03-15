package vn.prostylee.media.service.impl;

import com.amazonaws.AmazonClientException;
import io.jsonwebtoken.lang.Collections;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.core.constant.AppConstant;
import vn.prostylee.core.constant.ErrorResponseStatus;
import vn.prostylee.core.exception.ResourceNotFoundException;
import vn.prostylee.media.configuration.AwsS3Properties;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.exception.FileUploaderException;
import vn.prostylee.media.provider.async.AwsS3AsyncProvider;
import vn.prostylee.media.service.AttachmentService;
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

    private final AwsS3AsyncProvider awsS3AsyncProvider;
    private final AttachmentService attachmentService;
    private final String cloudfrontUrl;
    private final String s3MediaPublicFolder;
    private final String s3ResizePrefix;

    @Autowired
    public AwsS3ServiceImpl(
            AwsS3Properties awss3Properties,
            AwsS3AsyncProvider awsS3AsyncProvider,
            AttachmentService attachmentService) {
        this.awsS3AsyncProvider = awsS3AsyncProvider;
        this.attachmentService = attachmentService;
        this.cloudfrontUrl = awss3Properties.getCloudFrontUrl();
        this.s3MediaPublicFolder = awss3Properties.getS3MediaPublicFolder();
        this.s3ResizePrefix = awss3Properties.getS3ResizeImagePrefix();
    }

    @Override
    public List<String> getFileUrls(List<Long> fileIds) {
        return getImageUrls(fileIds, 0, 0);
    }

    @Override
    public String getImageUrl(Long id, int width, int height) {
        return generateUrlByDimension(attachmentService.getById(id), width, height);
    }

    @Override
    public List<String> getImageUrls(List<Long> fileIds, int width, int height) {
        List<Attachment> attachments = attachmentService.getByIds(fileIds);
        if(Collections.isEmpty(attachments)) {
            throw new ResourceNotFoundException("Files are not existed by getting with ids: " + fileIds);
        }
        return generateUrlsByDimension(attachments, width, height);
    }

    private List<String> generateUrlsByDimension(List<Attachment> attachments, int width, int height) {
        return attachments.stream()
                .map(attachment -> generateUrlByDimension(attachment, width, height))
                .collect(Collectors.toList());
    }

    private String generateUrlByDimension(Attachment attachment, int width, int height) {
        return buildUrl(attachment,width, height);
    }

    private String buildUrl(Attachment attachment, int width, int height){
        String prefix = cloudfrontUrl;
        if (width > 0 && height > 0) {
            prefix = String.format("%s%s%dx%d%s", cloudfrontUrl,
                    s3ResizePrefix, width, height, AppConstant.PATH_SEPARATOR);
        }
        return prefix + attachment.getPath() + AppConstant.PATH_SEPARATOR + attachment.getName();
    }

    @Override
    public List<AttachmentResponse> uploadFiles(List<MultipartFile> files) {
        return uploadFiles(s3MediaPublicFolder, files);
    }

    @Override
    public List<AttachmentResponse> uploadFiles(String folderName, List<MultipartFile> files) {
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
                futures.add(awsS3AsyncProvider.uploadFile(folderName, file));
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
            List<Attachment> attachments = attachmentService.getByIds(fileIds);
            for(Attachment attachment : attachments) {
                fileNames.add(attachment.getName());
            }
            attachmentService.deleteAttachmentsByIdIn(fileIds);
            awsS3AsyncProvider.deleteFiles(fileNames);
            return true;
        } catch (IllegalArgumentException | AmazonClientException e) {
            throw new FileUploaderException(ErrorResponseStatus.FILE_DELETE_ERROR.getCode(), e);
        }
    }

}
