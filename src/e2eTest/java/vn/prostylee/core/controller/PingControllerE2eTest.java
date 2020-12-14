package vn.prostylee.core.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import vn.prostylee.BaseRestControllerE2eTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class PingControllerE2eTest extends BaseRestControllerE2eTest {

    @Test
    void callPing_ReturnOkStatus() {
        given().log().all()
                .when()
                .get("/ping")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is("Server is running"));
    }
}
