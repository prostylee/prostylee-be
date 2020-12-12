package vn.prostylee.bdd;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Tag("E2eTest")
@Configuration
@ComponentScan({ "vn.prostylee.bdd" })
public class SpringE2eConfig {

    @Bean
    public WireMockServer wireMockServer() {
        log.info("Start wire mock server");
        WireMockServer server = new WireMockServer(
                WireMockConfiguration.options()
                .port(9095)
        );
        server.start();
        return server;
    }
}
