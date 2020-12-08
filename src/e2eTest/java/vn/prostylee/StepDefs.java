package vn.prostylee;

import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class StepDefs extends SpringE2eTest {

    @Autowired
    protected RestTemplate restTemplate;

    @When("^the client calls {string}$")
    public void the_client_calls_endpoint(String endpoint) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://localhost:8090/api/" + endpoint, String.class);
    }
}