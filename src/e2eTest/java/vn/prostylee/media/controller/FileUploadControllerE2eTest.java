package vn.prostylee.media.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import vn.prostylee.BaseRestControllerE2eTest;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class FileUploadControllerE2eTest extends BaseRestControllerE2eTest {

    private String accessToken;

    private static final String FILE1 = "images/img-test-01.png";

    @BeforeAll
    public void beforeAll() {
        accessToken = getAccessToken();
    }

    @Test
    void upload_file_Successfully() throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(FILE1);
        File file = new File(resource.toURI());
        given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("multipart/form-data")
                .multiPart("file", file)
                .when()
                .post("/v1/media/files")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(notNullValue()), "path", is(notNullValue()))
        ;
    }
}
