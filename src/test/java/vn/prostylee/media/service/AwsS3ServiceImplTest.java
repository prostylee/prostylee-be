package vn.prostylee.media.service;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import vn.prostylee.media.entity.Attachment;
import vn.prostylee.media.provider.async.AwsS3AsyncProvider;
import vn.prostylee.media.repository.AttachmentRepository;
import vn.prostylee.media.service.impl.AwsS3ServiceImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

/**
 * This class tests the {@link AwsS3ServiceImpl} class.
 */
public class AwsS3ServiceImplTest {

    private static final String BUCKET_NAME = "testaws";
    private static final String REGION = "ap-southeast-1";
    private static final int size = 30;
    private static final String IMAGE_PNG = "image/png";
    private static final String IMAGE_NAME = "b94ce7c3-ed51-4a5e-97a4-59824baf286c";
    private static final String BUCKET_HOST_NAME = "https://testaws.s3.amazonaws.com/";
    private static final String SAMPLE_IMAGE_URL = BUCKET_HOST_NAME + IMAGE_NAME;
    private static final String SAMPLE_IMAGE_SIZE_URL = BUCKET_HOST_NAME + size + "x" + size + "/" + IMAGE_NAME;

    private FileUploadService fileUploadService;
    @Mock
    private AmazonS3 s3Client;
    @Mock
    private AttachmentRepository repository;
    @Mock
    private AwsS3AsyncProvider provider;
    private MockMultipartFile image;
    private final List<String> ids = prepareIdsAsString();
    private final List<Long> idsAsLong = prepareIdsAsLong();
    private final List<Attachment> attachments = prepareAttachments();

    @BeforeEach
    void setUp() throws MalformedURLException {
        openMocks(this);
        fileUploadService = new AwsS3ServiceImpl(BUCKET_NAME, provider, repository);
        image = new MockMultipartFile("IMAGE_UPLOAD_PARAM_NAME", "test.png",
                IMAGE_PNG, "1234567890".getBytes());
        URL url = new URL(SAMPLE_IMAGE_URL);
        when(s3Client.getUrl(eq(BUCKET_NAME), anyString())).thenReturn(url);
    }

    @Test
    public void should_return_listUrlsWithoutSize_When_GetFilesWithZeroSize() {
        when(repository.findAllById(idsAsLong)).thenReturn(attachments);
        List<String> results = fileUploadService.getFiles(ids, 0, 0);
        Assert.assertEquals(SAMPLE_IMAGE_URL, results.get(0));
    }

    @Test
    public void should_return_listUrlsWithSize_When_GetFilesWithSize() {
        when(repository.findAllById(idsAsLong)).thenReturn(attachments);
        List<String> results = fileUploadService.getFiles(ids, size, size);
        Assert.assertEquals(SAMPLE_IMAGE_SIZE_URL, results.get(0));
    }

    private List<String> prepareIdsAsString() {
        String[] ids = new String[] {"1","2","3"};
        return Arrays.asList(ids);
    }

    private List<Long> prepareIdsAsLong() {
        Long[] ids = new Long[] {1L,2L,3L};
        return Arrays.asList(ids);
    }

    private List<Attachment> prepareAttachments() {
        List<Attachment> attachments = new ArrayList<>();
        Attachment attachment = new Attachment();
        attachment.setPath(SAMPLE_IMAGE_URL);
        attachments.add(attachment);
        return attachments;
    }
}
