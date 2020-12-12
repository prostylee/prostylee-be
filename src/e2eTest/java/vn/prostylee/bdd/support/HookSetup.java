package vn.prostylee.bdd.support;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vn.prostylee.bdd.SpringE2eConfig;

@Slf4j
@RequiredArgsConstructor
@CucumberContextConfiguration
@SpringBootTest(classes = SpringE2eConfig.class)
@ActiveProfiles({ "test", "e2e-test" })
@Tag("E2eTest")
public class HookSetup {

    private final WireMockServer wireMockServer;

    @Before
    public void before(Scenario scenario) {
        log.info("Before scenario - {}", scenario.getName());
    }

    @After
    public void after(Scenario scenario) {
        log.info("After scenario - {}", scenario.getName());
        cleanUpWireMock();
    }

    private void cleanUpWireMock() {
        wireMockServer.resetRequests();
        wireMockServer.resetMappings();
    }
}
