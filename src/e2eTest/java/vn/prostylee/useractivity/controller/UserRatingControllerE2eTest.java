package vn.prostylee.useractivity.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import vn.prostylee.BaseRestControllerE2eTest;
import vn.prostylee.useractivity.request.UserRatingRequestTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserRatingControllerE2eTest extends BaseRestControllerE2eTest {

    @Test
    void create_Successfully() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when().post("/v1/user-ratings")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()))
                .body("tokenType", is("Bearer"));
    }

    private UserRatingRequestTest createRequest() {
        return UserRatingRequestTest.builder()
                .targetId(Long.valueOf(1))
                .targetType("product")
                .value(5)
                .build();
    }
}