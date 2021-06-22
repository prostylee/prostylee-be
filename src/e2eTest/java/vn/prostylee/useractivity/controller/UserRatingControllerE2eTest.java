package vn.prostylee.useractivity.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import vn.prostylee.BaseRestControllerE2eTest;
import vn.prostylee.useractivity.request.UserRatingRequestTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserRatingControllerE2eTest extends BaseRestControllerE2eTest {
    private String accessToken;

    @BeforeAll
    public void beforeAll() {
        accessToken = getAccessToken();
    }

    @Test
    void create_Successfully() {
        given()
                .log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .post("/v1/user-ratings")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
    }

    @Test
    void update_Successfully() {
        given()
                .log().all()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .put("/v1/user-ratings/1")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", is(notNullValue()));
    }

    private UserRatingRequestTest createRequest() {
        return UserRatingRequestTest.builder()
                .targetId(Long.valueOf(1))
                .targetType("product")
                .value(5)
                .content("Test")
                .build();
    }
}