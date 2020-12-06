package vn.prostylee.auth.service.impl;

import com.google.firebase.auth.FirebaseToken;
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
import vn.prostylee.auth.dto.AuthUserDetails;
import vn.prostylee.auth.dto.request.LoginSocialRequest;
import vn.prostylee.auth.dto.response.JwtAuthenticationToken;
import vn.prostylee.auth.dto.response.ZaloResponse;
import vn.prostylee.auth.entity.User;
import vn.prostylee.auth.entity.UserLinkAccount;
import vn.prostylee.auth.service.AuthenticationService;
import vn.prostylee.auth.service.UserLinkAccountService;
import vn.prostylee.auth.service.UserService;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class ZaloAuthServiceImpl extends AuthenticationServiceCommon implements AuthenticationService {
    public static final String REQUIRED_FIELDS_FOR_PARAMS = "id,birthday,name,gender,picture";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String FIELDS_KEY = "fields";
    public static final String ZALO_VERSION = "v2.0";
    public static final String ZALO_ENDPOINT_GET_PROFILE = "me";
    public static final String ZALO_MAIN_API = "https://graph.zalo.me";
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
        ZaloResponse response = call(client, request.getIdToken());
        Optional<UserLinkAccount> userLinkAccount  = userLinkAccountService.getUserLinkAccountBy(response.getId());

        if(userLinkAccount.isPresent()){
            return processExist(userLinkAccount);
        }else{
            return processNew(response);
        }
    }

    private JwtAuthenticationToken processNew(ZaloResponse response) {
        User user = userService.save(response);
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private JwtAuthenticationToken processExist(Optional<UserLinkAccount> linkAccount) {
        User user = linkAccount.get().getUser();
        AuthUserDetails authUserDetails = new AuthUserDetails(user, null);
        return this.createResponse(authUserDetails);
    }

    private ZaloResponse call(WebClient client, String idToken) {
        return  client.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.path("/{version}/{endpoint}").queryParam(ACCESS_TOKEN_KEY, idToken)
                .queryParam(FIELDS_KEY, REQUIRED_FIELDS_FOR_PARAMS).build(ZALO_VERSION, ZALO_ENDPOINT_GET_PROFILE))
                .retrieve()
                .bodyToMono(ZaloResponse.class)
                .block();
    }

    private WebClient getWebClient(TcpClient tcpClient) {
        return WebClient.builder().baseUrl(ZALO_MAIN_API)
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultCookie("cookieKey", "cookieValue")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
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
