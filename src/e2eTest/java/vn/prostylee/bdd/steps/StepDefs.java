package vn.prostylee.bdd.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StepDefs {

    @When("the client calls the ping endpoint")
    public void theClientCallsPing() {
        throw new io.cucumber.java.PendingException();
    }

    @Then("the client receives status code of {int}")
    public void theClientReceivesStatusCodeOf(int status) {
        throw new io.cucumber.java.PendingException();
    }

    @And("the client receives a text is {string}")
    public void theClientReceivesATextIs(String message) {
        throw new io.cucumber.java.PendingException();
    }
}
