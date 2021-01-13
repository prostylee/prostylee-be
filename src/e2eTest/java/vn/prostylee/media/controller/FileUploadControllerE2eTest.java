package vn.prostylee.media.controller;

import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import vn.prostylee.BaseRestControllerE2eTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class FileUploadControllerE2eTest extends BaseRestControllerE2eTest {

    private String accessToken;
    private Response response;

    private static final String FILE1 = "images/img-test-01.png";
    private static final String UPLOAD_URL = "/v1/media/files";
    private static final String GET_IMAGE_URL = "/v1/media/images";
    private static final String IMAGE_CONDITION = "?w=150&h=150";

    @BeforeAll
    public void beforeAll() {
        accessToken = getAccessToken();
    }

    @Test
    @Order(1)
    void upload_file_successfully() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(FILE1);
        File file = new File(resource.toURI());
        response = given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("multipart/form-data")
                .multiPart("files", file, "image/png")
                .when()
                .post(UPLOAD_URL);
        response.then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(notNullValue()), "path", is(notNullValue()));
    }

    @Test
    @Order(2)
    void get_uploadedFile_bySize_successfully() throws IOException {
        List<Long> ids = response.jsonPath().getList("id");
        String idsAsString = StringUtils.join(ids, ",");
        Response urls = given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .get(GET_IMAGE_URL + "/" + idsAsString + IMAGE_CONDITION);
        List<String> paths = urls.jsonPath().get();

        // Verify
        BufferedImage bimg = ImageIO.read(new URL(paths.get(0)));
        if (bimg.getWidth() > bimg.getHeight()) {
            Assert.assertEquals(150, bimg.getWidth());
        } else {
            Assert.assertEquals(150, bimg.getHeight());
        }
        urls.then()
                .log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
