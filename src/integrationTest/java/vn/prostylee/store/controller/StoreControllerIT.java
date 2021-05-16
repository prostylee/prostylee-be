package vn.prostylee.store.controller;

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
import vn.prostylee.AuthSupporterIT;
import vn.prostylee.IntegrationTest;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.store.constants.StoreStatus;
import vn.prostylee.store.dto.request.StoreRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
@Sql(
        scripts = { "/data/database/store.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@Sql(
        statements = { "DELETE FROM store_statistic; DELETE FROM store; DELETE FROM company;" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
class StoreControllerIT extends AuthSupporterIT {

    private static final String ENDPOINT = "/v1/stores";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    void getAll_Successfully() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get(ENDPOINT))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(8))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content.length()").value(8))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void getAll_WithFilter_Successfully() throws Exception {
        final int pageSize = 3;
        this.mockMvc
                .perform(get(ENDPOINT)
                        .param("page", "0")
                        .param("limit", pageSize + "")
                        .param("keyword", "Prostylee 000")
                        .param("companyId", "1")
                        .param("ownerId", "1")
                        .param("status", "1")
                        .param("sorts", "-name")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.content.length()").value(pageSize))
                .andExpect(jsonPath("$.content[0].id").value(10))
                .andExpect(jsonPath("$.content[1].id").value(9))
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
                .andExpect(jsonPath("$.name").value("Prostylee 00010"))
                .andExpect(jsonPath("$.description").value("This is a description"))
                .andExpect(jsonPath("$.address").value("01 Ly Tu Trong"))
                .andExpect(jsonPath("$.website").value("https://prostylee10.vn"))
                .andExpect(jsonPath("$.phone").value("0988000111"))
                .andExpect(jsonPath("$.company.id").value(1))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.locationId").value(1))
                .andExpect(jsonPath("$.status").value(StoreStatus.ACTIVE.getValue()))
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
        StoreRequest request = StoreRequest.builder()
                .name("Prostylee Store 11")
                .description("Add new store")
                .address("Address new")
                .website("https://store-new.prostylee.vn")
                .phone("0988000112")
                .status(StoreStatus.IN_PROGRESS.getValue())
                .ownerId(1L)
                .locationId(1L)
                .companyId(1L)
                .build();

        MvcResult mvcResult = this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.website").value(request.getWebsite()))
                .andExpect(jsonPath("$.phone").value(request.getPhone()))
                .andExpect(jsonPath("$.company.id").value(request.getCompanyId()))
                .andExpect(jsonPath("$.ownerId").value(request.getOwnerId()))
                .andExpect(jsonPath("$.locationId").value(request.getLocationId()))
                .andExpect(jsonPath("$.status").value(request.getStatus()))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void create_MissingRequiredField_Failed() throws Exception {
        StoreRequest request = StoreRequest.builder()
                .address("Add new 11")
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
        StoreRequest request = StoreRequest.builder()
                .name("Prostylee Store 10 updated")
                .description("Updated store 10")
                .address("Address updated")
                .website("https://store-updated.prostylee.vn")
                .phone("0988567111")
                .status(StoreStatus.IN_PROGRESS.getValue())
                .ownerId(1L)
                .locationId(1L)
                .companyId(1L)
                .build();

        this.mockMvc
                .perform(put(ENDPOINT + "/" + id)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(request.getName()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.website").value(request.getWebsite()))
                .andExpect(jsonPath("$.phone").value(request.getPhone()))
                .andExpect(jsonPath("$.company.id").value(request.getCompanyId()))
                .andExpect(jsonPath("$.ownerId").value(request.getOwnerId()))
                .andExpect(jsonPath("$.locationId").value(request.getLocationId()))
                .andExpect(jsonPath("$.status").value(request.getStatus()))
                .andReturn();
    }

    @Test
    void update_EntityNotFound_Failed() throws Exception {
        final long id = 20;
        StoreRequest request = StoreRequest.builder()
                .name("Prostylee Store updated")
                .companyId(1L)
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
        final long id = 10L;
        this.mockMvc
                .perform(delete(ENDPOINT + "/" + id))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data").value("true"))
                .andReturn();
    }

    @Test
    void delete_EntityNotFound_Successfully() throws Exception {
        final long id = 10000L;
        this.mockMvc
                .perform(delete(ENDPOINT + "/" + id))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data").value("false"))
                .andReturn();
    }
}
