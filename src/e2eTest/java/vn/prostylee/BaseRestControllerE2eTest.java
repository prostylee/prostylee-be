package vn.prostylee;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

@E2eTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseRestControllerE2eTest {

    @BeforeAll
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
        RestAssured.port = 8090;
    }
}
