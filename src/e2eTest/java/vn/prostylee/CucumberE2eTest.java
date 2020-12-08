package vn.prostylee;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/e2eTest/resources/features",
//        plugin = {"pretty", "html:target/cucumber"},
        extraGlue = "vn.prostylee"
)
public class CucumberE2eTest extends SpringE2eTest {
}
