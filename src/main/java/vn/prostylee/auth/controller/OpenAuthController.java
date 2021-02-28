package vn.prostylee.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.prostylee.auth.dto.request.OAuthRequest;
import vn.prostylee.auth.service.OAuthService;
import vn.prostylee.core.constant.ApiVersion;
import vn.prostylee.core.dto.response.SimpleResponse;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = ApiVersion.API_V1 + "/oauth")
@RequiredArgsConstructor
public class OpenAuthController {

    private final OAuthService oAuthService;

    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.OK)
    public SimpleResponse signUp(@Valid @RequestBody OAuthRequest request) {
        return oAuthService.signUp(request);
    }
}