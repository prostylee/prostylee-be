package vn.prostylee.core.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("app.http")
public class HttpProperties {

    @Value("${app.http.connectTimeout:5000}")
    private int connectTimeout;

    @Value("${app.http.responseTimeout:5000}")
    private int responseTimeoutInMs;

    @Value("${app.http.httpUseProxy:false}")
    private boolean httpUseProxy;

    @Value("${app.http.proxyHost:localhost}")
    private String proxyHost;

    @Value("${app.http.proxyPort:3128}")
    private int proxyPort;
}
