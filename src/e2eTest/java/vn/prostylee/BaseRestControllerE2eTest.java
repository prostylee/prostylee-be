package vn.prostylee;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.MediaType;
import vn.prostylee.auth.request.RegisterRequest;
import vn.prostylee.auth.response.AuthTokenResponse;

import static io.restassured.RestAssured.given;

@E2eTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseRestControllerE2eTest {

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = 8090;
    }

    protected String getAccessToken() {
        AuthTokenResponse response = given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(createRequest())
                .when()
                .post("/v1/auth/sign-up")
                .then()
                .log().all()
                .extract()
                .as(AuthTokenResponse.class);
        return response.getAccessToken();
    }

    private RegisterRequest createRequest() {
        return RegisterRequest.builder()
                .fullName("E2E test user")
                .username("e2dtest" + System.currentTimeMillis() + "@test.prostylee.com")
                .password("12345678")
                .build();
    }
}
