package vn.prostylee.media.provider;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import vn.prostylee.ConfigTestUtils;
import vn.prostylee.media.configuration.AWSS3Properties;
import vn.prostylee.media.dto.response.AttachmentResponse;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.provider.async.AwsS3AsyncProvider;
import vn.prostylee.media.service.AttachmentService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static vn.prostylee.ConfigTestUtils.FOLDER;
import static vn.prostylee.ConfigTestUtils.mockMultipartFile;

/**
 * This class tests the {@link AwsS3AsyncProvider} class.
 */
public class AwsS3AsyncProviderTest {

    private AwsS3AsyncProvider awsS3AsyncProvider;
    @Mock
    private AmazonS3 s3Client;
    @Mock
    private AttachmentService attachmentService;
    private final AWSS3Properties awss3Properties = ConfigTestUtils.prepareAwsS3Props();
    private final List<String> fileNames = Arrays.asList("deletedFile");

    @BeforeEach
    void setUp() throws MalformedURLException {
        openMocks(this);
        awsS3AsyncProvider = new AwsS3AsyncProvider(s3Client, awss3Properties, attachmentService);
    }

    @Test
    void should_throwException_when_uploadFile_GetException() throws IOException {
        doThrow(new AmazonServiceException("failed")).when(s3Client).putObject(any(), any(), any(), any());
        AmazonServiceException exception = assertThrows(
                AmazonServiceException.class, () -> awsS3AsyncProvider.uploadFile(FOLDER, mockMultipartFile));
    }

    @Test
    public void should_return_attachments_when_uploadFilesSuccessfully() throws IOException, ExecutionException, InterruptedException {
        URL mockUrl = new URL("http://host.com");
        when(s3Client.getUrl(any(), any())).thenReturn(mockUrl);
        when(attachmentService.saveAttachmentByUploadFile(any(), any())).thenReturn(new Attachment());
        Future<AttachmentResponse> result = awsS3AsyncProvider.uploadFile(FOLDER, mockMultipartFile);
        assertThat(result.get(), is(notNullValue()));
    }

    @Test
    public void should_throwException_when_deleteFilesGetException() {
        doThrow(new AmazonServiceException("failed")).when(s3Client).deleteObjects(any());
        AmazonServiceException exception = assertThrows(
                AmazonServiceException.class, () -> awsS3AsyncProvider.deleteFiles(fileNames));
    }

    @Test
    public void should_return_true_when_deleteFilesSuccessfully() throws ExecutionException, InterruptedException {
        DeleteObjectsResult results =
                new DeleteObjectsResult(Arrays.asList(new DeleteObjectsResult.DeletedObject()));
        when(s3Client.deleteObjects(any())).thenReturn(results);
        Future<Boolean> result = awsS3AsyncProvider.deleteFiles(fileNames);
        assertThat(result.get(), is(true));
    }
}
