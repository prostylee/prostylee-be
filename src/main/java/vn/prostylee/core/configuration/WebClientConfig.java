package vn.prostylee.core.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import reactor.netty.transport.ProxyProvider;
import vn.prostylee.core.configuration.properties.HttpProperties;
import vn.prostylee.core.constant.TracingConstant;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebClientConfig {

    private final HttpProperties httpProperties;

    @Bean
    public WebClient webClient(TcpClient tcpClient) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(client -> tcpClient)
                .compress(true);

        httpClient = httpClient.compress(true);
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(TracingConstant.CORRELATION_ID, UUID.randomUUID().toString())
                .filter(requestProcessor())
                .filter(responseProcessor())
                .build();
    }

    @Bean
    public TcpClient tcpClient() {
        TcpClient client = TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, httpProperties.getConnectTimeout())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(httpProperties.getResponseTimeoutInMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(httpProperties.getResponseTimeoutInMs(), TimeUnit.MILLISECONDS));
                });

        if (httpProperties.isHttpUseProxy()) {
            client = client.proxy(proxy -> proxy.type((ProxyProvider.Proxy.HTTP))
                    .host(httpProperties.getProxyHost())
                    .port(httpProperties.getProxyPort()));
        }

        return client;
    }

    private ExchangeFilterFunction requestProcessor() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.debug("WebClient request, correlationId={}, requestTime={}",
                    request.headers().getFirst(TracingConstant.CORRELATION_ID), LocalDateTime.now());
            return Mono.just(request);
        });
    }

    private ExchangeFilterFunction responseProcessor() {
        return (request, next) -> next.exchange(request).flatMap(response -> {
            String correlationId = Optional.of(response.headers())
                    .map(ClientResponse.Headers::asHttpHeaders)
                    .map(httpHeaders -> httpHeaders.getFirst(TracingConstant.CORRELATION_ID))
                    .orElseGet(() -> request.headers().getFirst(TracingConstant.CORRELATION_ID));
            log.debug("WebClient response, correlationId={}, responseTime={}", correlationId, LocalDateTime.now());
            return Mono.just(response);
        });
    }
}
