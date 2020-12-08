package vn.prostylee;

import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Tag("E2eTest")
@CucumberContextConfiguration
@SpringBootTest(classes = ProStyleeApplication.class)
@ActiveProfiles({ "test", "e2e-test" })
public class SpringE2eTest {
}
