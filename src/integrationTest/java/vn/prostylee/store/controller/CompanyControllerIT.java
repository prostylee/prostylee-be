package vn.prostylee.store.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.AuthSupporterIT;
import vn.prostylee.IntegrationTest;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.store.dto.request.CompanyRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
@Sql(
        scripts = { "/data/database/company.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@Sql(
        statements = { "DELETE FROM company" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
class CompanyControllerIT extends AuthSupporterIT {

    private static final String ENDPOINT = "/v1/companies";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void getAll_Successfully() throws Exception {
        this.mockMvc
                .perform(get(ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(8))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content.length()").value(8))
                .andReturn();
    }

    @Test
    void getAll_WithFilter_Successfully() throws Exception {
        final int pageSize = 3;
        this.mockMvc
                .perform(get(ENDPOINT)
                        .param("page", "0")
                        .param("limit", pageSize + "")
                        .param("keyword", "Company 000")
                        .param("sorts", "-active", "name")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.content.length()").value(pageSize))
                .andExpect(jsonPath("$.content[0].id").value(6))
                .andExpect(jsonPath("$.content[1].id").value(10))
                .andReturn();
    }

    @Test
    void getById_Successfully() throws Exception {
        final long id = 10L;
        this.mockMvc
                .perform(get(ENDPOINT + "/" + id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Prostylee Company 00010"))
                .andExpect(jsonPath("$.description").value("This is a description"))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.active").value(true))
                .andReturn();
    }

    @Test
    void getById_EntityNotFound_Failed() throws Exception {
        final long id = 20L;

        this.mockMvc
                .perform(get(ENDPOINT + "/" + id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void create_Successfully() throws Exception {
        super.setAuth();
        CompanyRequest request = CompanyRequest.builder()
                .name("Prostylee Company 11")
                .description("Add new 11")
                .build();

        this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value("Prostylee Company 11"))
                .andExpect(jsonPath("$.description").value("Add new 11"))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.active").value(true))
                .andReturn();
    }

    @Test
    void create_MissingRequiredField_Failed() throws Exception {
        CompanyRequest request = CompanyRequest.builder()
                .description("Add new 11")
                .build();

        this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void update_Successfully() throws Exception {
        final long id = 10;
        CompanyRequest request = CompanyRequest.builder()
                .name("Prostylee Company updated")
                .description("Updated company name")
                .active(false)
                .build();

        this.mockMvc
                .perform(put(ENDPOINT + "/" + id)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Prostylee Company updated"))
                .andExpect(jsonPath("$.description").value("Updated company name"))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.active").value(false))
                .andReturn();
    }

    @Test
    void update_EntityNotFound_Failed() throws Exception {
        final long id = 20;
        CompanyRequest request = CompanyRequest.builder()
                .name("Prostylee Company updated")
                .description("Updated company not found")
                .active(false)
                .build();

        this.mockMvc
                .perform(put(ENDPOINT + "/" + id)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void delete_Successfully() throws Exception {
        final long userId = 10L;
        this.mockMvc
                .perform(delete(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("true"))
                .andReturn();
    }

    @Test
    void delete_EntityNotFound_Successfully() throws Exception {
        final long userId = 20L;
        this.mockMvc
                .perform(delete(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").value("false"))
                .andReturn();
    }
}
