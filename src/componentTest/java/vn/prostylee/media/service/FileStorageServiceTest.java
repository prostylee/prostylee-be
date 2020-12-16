package vn.prostylee.media.service;

import vn.prostylee.ComponentTest;
import vn.prostylee.media.mock.HttpServletRequestMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.media.dto.request.FileStorageItemRequest;
import vn.prostylee.media.dto.request.FileStorageRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@ComponentTest
public class FileStorageServiceTest {

    private static final String FILE1 = "images/img-test-01.png";

    private static final String FILE2 = "images/img-test-02.png";

    @Autowired
    protected WebApplicationContext webAppContext;

    @Autowired
    private FileStorageService fileStorageService;

    private HttpServletRequest request = Mockito.spy(HttpServletRequestMock.class);

    private List<MultipartFile> files;

    @BeforeEach
    public void setup() throws Exception {
        MockMvcBuilders.webAppContextSetup(webAppContext).build();// Standalone context

        InputStream is1 = this.getClass().getClassLoader().getResourceAsStream(FILE1);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("file", FILE1, "multipart/form-data", is1);

        InputStream is2 = this.getClass().getClassLoader().getResourceAsStream(FILE2);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", FILE2, "multipart/form-data", is2);

        files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);
    }

    @Test
    public void testUploadFiles() {
        List<FileStorageItemRequest> types = new ArrayList<>();
        FileStorageItemRequest itemDms = new FileStorageItemRequest();
        itemDms.setName("DMS");
        itemDms.setType(1);
        types.add(itemDms);

        FileStorageItemRequest itemHRMS = new FileStorageItemRequest();
        itemHRMS.setName("HRMS");
        itemHRMS.setType(2);
        types.add(itemHRMS);

        FileStorageRequest fileStorageRequest = new FileStorageRequest();
        fileStorageRequest.setTypes(types);

        List<FileStorageResponse> responses = fileStorageService.uploadFiles(fileStorageRequest, files, request);

        Assertions.assertEquals(types.size(), responses.size());

        // Verify file 1
        Assertions.assertEquals(itemDms.getType(), responses.get(0).getType());
        Assertions.assertEquals(FILE1, responses.get(0).getDisplayName());
        Assertions.assertNotNull(responses.get(0).getFilePath());
        Assertions.assertNotNull(responses.get(0).getFileUrl());
        Assertions.assertNotNull(responses.get(0).getCreatedDate());

        // Verify file 2
        Assertions.assertEquals(itemHRMS.getType(), responses.get(1).getType());
        Assertions.assertEquals(FILE2, responses.get(1).getDisplayName());
        Assertions.assertNotNull(responses.get(1).getFilePath());
        Assertions.assertNotNull(responses.get(1).getFileUrl());
        Assertions.assertNotNull(responses.get(1).getCreatedDate());

        System.out.println("responses " + responses);

        // Test load and delete file 1
        testLoadFileAsResource(responses.get(0).getFilePath());
        testDeleteFile(responses.get(0).getFilePath());

        // Test load and delete file 2
        testLoadFileAsResource(responses.get(1).getFilePath());
        testDeleteFile(responses.get(1).getFilePath());
    }

    public void testLoadFileAsResource(String fileName) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        Assertions.assertTrue(resource.exists());
    }

    private void testDeleteFile(String fileName) {
        Assertions.assertTrue(fileStorageService.deleteFile(fileName));
    }
}
