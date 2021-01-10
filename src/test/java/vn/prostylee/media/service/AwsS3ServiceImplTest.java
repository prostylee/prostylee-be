package vn.prostylee.media.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import vn.prostylee.ConfigTestUtils;
import vn.prostylee.core.constant.ErrorResponseStatus;
import vn.prostylee.media.configuration.AWSS3Properties;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.exception.FileUploaderException;
import vn.prostylee.media.provider.async.AwsS3AsyncProvider;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.impl.AwsS3ServiceImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static vn.prostylee.ConfigTestUtils.FOLDER;
import static vn.prostylee.ConfigTestUtils.files;

/**
 * This class tests the {@link AwsS3ServiceImpl} class.
 */
public class AwsS3ServiceImplTest {

    private static final int size = 30;
    private static final String IMAGE_NAME = "b94ce7c3-ed51-4a5e-97a4-59824baf286c";
    private static final String SAMPLE_IMAGE_URL = ConfigTestUtils.BUCKET_HOST_NAME + IMAGE_NAME;
    private static final String SAMPLE_IMAGE_SIZE_URL = ConfigTestUtils.BUCKET_HOST_NAME + size + "x" + size + "/" + IMAGE_NAME;

    private FileUploadService fileUploadService;
    @Mock
    private AmazonS3 s3Client;
    @Mock
    private AttachmentRepository attachmentRepository;
    @Mock
    private AwsS3AsyncProvider awsS3AsyncProvider;
    @Captor
    private ArgumentCaptor<Attachment> attachmentArgumentCaptor;
    private final AWSS3Properties awss3Properties = ConfigTestUtils.prepareAwsS3Props();
    private final List<Long> attachmentIds = ConfigTestUtils.prepareAttachmentsIds();
    private final List<Attachment> attachments = prepareAttachments();

    @BeforeEach
    void setUp() throws MalformedURLException {
        openMocks(this);
        fileUploadService = new AwsS3ServiceImpl(awss3Properties, awsS3AsyncProvider, attachmentRepository);
        URL url = new URL(SAMPLE_IMAGE_URL);
        when(s3Client.getUrl(eq(ConfigTestUtils.BUCKET_NAME), anyString())).thenReturn(url);
    }

    @Test
    public void should_return_listUrlsWithoutSize_when_getFilesWithZeroSize() {
        when(attachmentRepository.findAllById(attachmentIds)).thenReturn(attachments);
        List<String> results = fileUploadService.getFiles(attachmentIds, 0, 0);
        Assert.assertEquals(SAMPLE_IMAGE_URL, results.get(0));
    }

    @Test
    public void should_return_listUrlsWithSize_when_getFilesWithSize() {
        when(attachmentRepository.findAllById(attachmentIds)).thenReturn(attachments);
        List<String> results = fileUploadService.getFiles(attachmentIds, size, size);
        Assert.assertEquals(SAMPLE_IMAGE_SIZE_URL, results.get(0));
    }
    @Test
    void should_throwException_when_uploadFile_GetException() throws IOException {
        doThrow(new IOException("failed")).when(awsS3AsyncProvider).uploadFile(any(), any());
        FileUploaderException exception = assertThrows(
                FileUploaderException.class, () -> fileUploadService.uploadFiles(FOLDER, files));
        assertThat(exception.getMessage(), is(equalTo(ErrorResponseStatus.FILE_UPLOAD_ERROR.getCode())));
    }

    @Test
    public void should_return_emptyList_when_uploadFilesWithEmptyFiles() {
        List<AttachmentResponse> results = fileUploadService.uploadFiles(FOLDER, new ArrayList<>());
        assertThat(results, is(empty()));
    }

    @Test
    public void should_return_attachments_when_uploadFilesSuccessfully() throws IOException, ExecutionException, InterruptedException {
        Future<AttachmentResponse> future = Mockito.mock(Future.class);
        when(future.get()).thenReturn(prepareResponse());
        when(awsS3AsyncProvider.isAllFutureDone(any())).thenReturn(true);
        when(awsS3AsyncProvider.uploadFile(FOLDER, files.get(0))).thenReturn(future);
        List<AttachmentResponse> results = fileUploadService.uploadFiles(FOLDER, files);
        assertThat(results, is(not(empty())));
        assertThat(results.get(0).getPath(), is(equalTo(SAMPLE_IMAGE_URL)));
    }

    @Test
    public void should_throwException_when_deleteFilesGetException() {
        doThrow(new AmazonClientException("failed")).when(awsS3AsyncProvider).deleteFiles(any());
        FileUploaderException exception = assertThrows(
                FileUploaderException.class, () -> fileUploadService.deleteFiles(attachmentIds));
        assertThat(exception.getMessage(), is(equalTo(ErrorResponseStatus.FILE_DELETE_ERROR.getCode())));
    }

    @Test
    public void should_return_true_when_deleteFilesSuccessfully() {
        boolean result = fileUploadService.deleteFiles(attachmentIds);
        verify(attachmentRepository, Mockito.times(1)).deleteAttachmentsByIdIn(attachmentIds);
        assertThat(result, is(true));
    }

    private List<Attachment> prepareAttachments() {
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setPath(SAMPLE_IMAGE_URL);
        attachments.add(attachment);
        return attachments;
    }

    private AttachmentResponse prepareResponse() {
        AttachmentResponse attachment = new AttachmentResponse();
        attachment.setPath(SAMPLE_IMAGE_URL);
        return attachment;
    }
}