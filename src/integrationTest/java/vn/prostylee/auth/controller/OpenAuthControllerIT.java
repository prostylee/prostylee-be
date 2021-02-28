package vn.prostylee.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.IntegrationTest;
import vn.prostylee.auth.dto.request.OAuthRequest;
import vn.prostylee.auth.dto.request.OAuthUserInfoRequest;
import vn.prostylee.core.utils.JsonUtils;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
public class OpenAuthControllerIT {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void signUp_Successfully() throws Exception {
        OAuthRequest request = createRequest();
        this.mockMvc.perform(
                post("/v1/oauth/sign-up")
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.username").value(request.getUsername()))
                .andExpect(jsonPath("$.data.fullName").value(request.getUserInfo().getName()))
                .andExpect(jsonPath("$.data.phoneNumber").value(request.getUserInfo().getPhoneNumber()))
                .andExpect(jsonPath("$.data.roles[0]").value("BUYER"))
                .andReturn();
    }

    private OAuthRequest createRequest() {
        final String email = "test" + System.currentTimeMillis() + "@prostylee.vn";
        return OAuthRequest.builder()
                .username(email)
                .userStatus("CONFIRMED")
                .enabled(true)
                .groups(Collections.singletonList("BUYER"))
                .userInfo(OAuthUserInfoRequest.builder()
                        .sub(UUID.randomUUID().toString())
                        .email(email)
                        .emailVerified(true)
                        .gender("M")
                        .phoneNumber("+84988985407")
                        .phoneNumberVerified(true)
                        .birthdate("2020-02-28")
                        .nickname("gpcoder1")
                        .preferredUsername("gpc1")
                        .familyName("Phan")
                        .givenName("Giang")
                        .middleName("Thanh")
                        .name("Giang Phan")
                        .profile("https://gpcoder.com/me/")
                        .picture("https://gpcoder.com/me/avatar.jpg")
                        .website("https://gpcoder.com")
                        .build())
                .build();
    }
}
