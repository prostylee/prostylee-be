package vn.prostylee.auth.service.impl;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import vn.prostylee.auth.constant.SocialProviderType;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.dto.response.ZaloResponse;
import vn.prostylee.auth.service.AuthenticationService;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;

import java.util.concurrent.TimeUnit;

@Component
public class ZaloAuthServiceImpl extends AuthenticationServiceCommon implements AuthenticationService {
    @Autowired
    private UserLinkAccountService userLinkAccountService;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    public SocialProviderType getProviderType() {
        return SocialProviderType.ZALO;
    }

    @Override
    public JwtAuthenticationToken login(LoginSocialRequest request) {
        TcpClient tcpClient = getTcpClient();
        WebClient client = getWebClient(tcpClient);
        Mono<ZaloResponse> result = call(client);
        return new JwtAuthenticationToken();
    }

    private Mono<ZaloResponse> call(WebClient client) {
        return  client.method(HttpMethod.GET).uri(uriBuilder -> uriBuilder
                .path("/{version}/me?access_token={token}&fields={fields}")
                .build("v2.0", "sjdkajkdjToken gui tu client len", "id,birthday,name,gender,picture" ))
                .retrieve().bodyToMono(ZaloResponse.class);
    }

    private WebClient getWebClient(TcpClient tcpClient) {
        WebClient client = WebClient
                .builder()
                .baseUrl("https://graph.zalo.me")
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        return client;
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected(connection -> {
                    connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                });
    }
}
