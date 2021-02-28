package vn.prostylee.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.IntegrationTest;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.core.utils.JsonUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
@Sql(
        scripts = { "/data/database/users.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@Sql(
        statements = { "DELETE FROM user_role; DELETE FROM user;" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
class UserControllerIT {

    private static final String ENDPOINT = "/v1/users";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void getAll_Successfully() throws Exception {
        final int totalOfRecords = 10;
        final int pageSize = 5;

        MvcResult mvcResult = this.mockMvc
                .perform(get(ENDPOINT)
                        .param("page", "0")
                        .param("limit", pageSize + "")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(totalOfRecords))
                .andExpect(jsonPath("$.totalPages").value(totalOfRecords /pageSize))
                .andExpect(jsonPath("$.content.length()").value(pageSize))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void getById_Successfully() throws Exception {
        final long userId = 1L;

        MvcResult mvcResult = this.mockMvc
                .perform(get(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("test1@prostylee.vn"))
                .andExpect(jsonPath("$.email").value("test1@prostylee.vn"))
                .andExpect(jsonPath("$.phoneNumber").value("0900000003"))
                .andExpect(jsonPath("$.fullName").value("Prostylee 1"))
                .andExpect(jsonPath("$.gender").value("M"))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void create_Successfully() throws Exception {
        final long userId = 11;
        UserRequest request = UserRequest.builder()
                .username(createEmail(userId))
                .fullName("Test 11")
                .password("1234")
                .build();

        MvcResult mvcResult = this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(createEmail(userId)))
                .andExpect(jsonPath("$.fullName").value("Test 11"))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void create_UsernameExists_Failed() throws Exception {
        UserRequest request = UserRequest.builder()
                .username("test1@prostylee.vn")
                .fullName("Test 1")
                .password("1234")
                .build();

        MvcResult mvcResult = this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void update_Successfully() throws Exception {
        final long userId = 1;
        UserRequest request = UserRequest.builder()
                .id(userId)
                .fullName("Test 12")
                .username(createEmail(userId))
                .build();

        MvcResult mvcResult = this.mockMvc
                .perform(put(ENDPOINT + "/" + userId)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(createEmail(userId)))
                .andExpect(jsonPath("$.fullName").value("Test 12"))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void delete_Successfully() throws Exception {
        final long userId = 1L;

        MvcResult mvcResult = this.mockMvc
                .perform(delete(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    private String createEmail(long userId) {
        return StringUtils.join("user", userId, "@prostylee.vn");
    }
}

