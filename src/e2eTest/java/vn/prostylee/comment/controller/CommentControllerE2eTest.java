package vn.prostylee.comment.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import vn.prostylee.BaseRestControllerE2eTest;
import vn.prostylee.comment.constant.CommentDestinationType;
import vn.prostylee.comment.request.UpdateRequestTest;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class CommentControllerE2eTest extends BaseRestControllerE2eTest {

    private String accessToken;

    @BeforeAll
    public void beforeAll() {
        accessToken = getAccessToken();
    }

    @Test
    void create_Successfully() {
        given().log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .post("/v1/comments")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()))
        ;
    }

    private UpdateRequestTest createRequest() {
        return UpdateRequestTest.builder()
                .attachmentId(Arrays.asList(Long.valueOf(100),Long.valueOf(101),Long.valueOf(102)))
                .targetId(Long.valueOf(1))
                .targetType(CommentDestinationType.STORE.getStatus())
                .content("This is test e2e Content")
                .parentId(Long.valueOf(1))
                .build();
    }
}
