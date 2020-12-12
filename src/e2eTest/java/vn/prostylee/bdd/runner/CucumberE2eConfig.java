package vn.prostylee.bdd.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        glue = { "vn.prostylee.bdd.steps", "vn.prostylee.bdd.support" },
        features = "classpath:features",
        plugin = {
                "pretty",
                "html:build/reports/cucumber/html",
                "json:build/reports/cucumber/cucumber.json",
                "usage:build/reports/cucumber/usage.json",
                "junit:build/reports/cucumber/junit.xml",
        },
        tags = "not @Ignore"
)
public class CucumberE2eConfig {
}
