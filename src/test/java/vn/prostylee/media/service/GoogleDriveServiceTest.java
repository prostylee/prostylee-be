package vn.prostylee.media.service;

import com.google.api.services.drive.model.File;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import vn.prostylee.ProStyleeApplication;
import vn.prostylee.media.mock.HttpServletRequestMock;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringJUnitWebConfig(ProStyleeApplication.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GoogleDriveServiceTest {

    private static final String FILE1 = "img-test-01.png";

    private static final String FILE2 = "img-test-02.png";

    @Autowired
    protected WebApplicationContext webAppContext;

    @Autowired
    private CloudStorageService cloudStorageService;

    private HttpServletRequest request = Mockito.spy(HttpServletRequestMock.class);

    private static String folderId;
    private static String fileName;

    @BeforeEach
    public void setup() {
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        MockMvcBuilders.webAppContextSetup(webAppContext).build();// Standalone context
    }

    private List<MultipartFile> createMultipartFiles() throws IOException {
        InputStream is1 = this.getClass().getClassLoader().getResourceAsStream(FILE1);
        MockMultipartFile mockMultipartFile1 = new MockMultipartFile("file", FILE1, "multipart/form-data", is1);

        InputStream is2 = this.getClass().getClassLoader().getResourceAsStream(FILE2);
        MockMultipartFile mockMultipartFile2 = new MockMultipartFile("file", FILE2, "multipart/form-data", is2);

        List<MultipartFile> files = new ArrayList<>();
        files.add(mockMultipartFile1);
        files.add(mockMultipartFile2);
        return files;
    }

    @Test
    @Order(1)
    public void test1CreateFolder() throws IOException {
        final File testFolder = cloudStorageService.createFolder("UT_" + LocalDateTime.now());
        System.out.println("testFolder " + testFolder);

        folderId = testFolder.getId();
        System.out.println("folderId " + folderId);
        Assertions.assertNotNull(folderId);
    }

    @Test
    @Order(2)
    public void test2UploadFiles() throws IOException {
        System.out.println("testUploadFiles folderId " + folderId);
        List<MultipartFile> files = createMultipartFiles();

        final List<File> responses = cloudStorageService.uploadFiles(folderId, files.toArray(new MultipartFile[0]));
        System.out.println("responses " + responses);
        Assertions.assertEquals(files.size(), responses.size());

        // Verify file 1
        Assertions.assertNotNull(responses.get(0).getId());
        Assertions.assertEquals(FILE1, responses.get(0).getName());
        Assertions.assertEquals("image/png", responses.get(0).getMimeType());
        Assertions.assertEquals("drive#file", responses.get(0).getKind());
        Assertions.assertEquals(folderId, responses.get(0).getParents().get(0));
        Assertions.assertNotNull(responses.get(0).getWebContentLink());

        // Verify file 2
        Assertions.assertNotNull(responses.get(1).getId());
        Assertions.assertEquals(FILE2, responses.get(1).getName());
        Assertions.assertEquals("image/png", responses.get(1).getMimeType());
        Assertions.assertEquals("drive#file", responses.get(1).getKind());
        Assertions.assertEquals(folderId, responses.get(1).getParents().get(0));
        Assertions.assertNotNull(responses.get(1).getWebContentLink());

        fileName = responses.get(0).getId();
    }

    @Test
    @Order(3)
    public void test3GetFilesInFolder() {
        List<File> responses = cloudStorageService.getFilesInFolder(folderId);
        System.out.println("getFilesInFolder " + responses);
        Assertions.assertTrue(responses.size() == 2);
    }

    @Test
    @Order(4)
    public void test4DeleteFile() {
        Assertions.assertTrue(cloudStorageService.deleteFile(fileName));
        List<File> responses = cloudStorageService.getFilesInFolder(folderId);
        Assertions.assertTrue(responses.size() == 1);
    }
}