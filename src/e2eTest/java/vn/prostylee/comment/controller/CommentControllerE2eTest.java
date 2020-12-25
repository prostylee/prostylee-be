package vn.prostylee.comment.controller;

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

    @Test
    void update_Successfully() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .put("/v1/comments")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()))
                .body("tokenType", is("Bearer"))
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
