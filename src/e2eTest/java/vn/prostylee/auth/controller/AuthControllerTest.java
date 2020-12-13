package vn.prostylee.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import vn.prostylee.BaseRestControllerE2eTest;
import vn.prostylee.auth.request.RegisterRequest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class AuthControllerTest extends BaseRestControllerE2eTest {

    @Test
    void register_Successfully() {
        given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .post("/v1/auth/sign-up")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()))
                .body("tokenType", is("Bearer"))
        ;
    }

    private RegisterRequest createRequest() {
        return RegisterRequest.builder()
                .fullName("Giang Phan")
                .username(createEmail(System.currentTimeMillis()))
                .gender('M')
                .password("12345678")
                .phoneNumber("0988985000")
                .build();
    }

    private String createEmail(long userId) {
        return StringUtils.join("user", userId, "@prostylee.vn");
    }
}
