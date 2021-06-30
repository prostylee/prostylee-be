package vn.prostylee.location.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.IntegrationTest;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.location.dto.request.LocationRequest;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@WebAppConfiguration
@Sql(
        scripts = { "/data/database/location.sql" },
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
@Sql(
        statements = { "DELETE FROM location;" },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
        config = @SqlConfig(transactionMode = SqlConfig.TransactionMode.ISOLATED)
)
class LocationControllerIT {

    private static final String ENDPOINT = "/v1/locations";

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

        this.mockMvc
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
    }

    @Test
    void getAll_Ids_Successfully() throws Exception {
        List<String> ids = Arrays.asList("1", "4", "8");
        MultiValueMap<String, String> idParams = new LinkedMultiValueMap<>();
        idParams.addAll("ids", ids);

        this.mockMvc
                .perform(get(ENDPOINT)
                        .params(idParams)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(ids.size()))
                .andReturn();
    }

//    @Test
//    void getById_Successfully() throws Exception {
//        final long id = 1L;
//        this.mockMvc
//                .perform(get(ENDPOINT + "/" + id))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(id))
//                .andExpect(jsonPath("$.address").value("18, Nguyễn văn Mại"))
//                .andExpect(jsonPath("$.latitude").value("10.806406363857086"))
//                .andExpect(jsonPath("$.longitude").value("106.6634168400805"))
//                .andExpect(jsonPath("$.city").value("Tân Bình"))
//                .andExpect(jsonPath("$.state").value("HCM"))
//                .andExpect(jsonPath("$.country").value("Việt Nam"))
//                .andExpect(jsonPath("$.zipcode").value("700000"))
//                .andReturn();
//    }

    @Test
    void create_Successfully() throws Exception {
        final long id = 11;
        LocationRequest request = LocationRequest.builder()
                .address("Test 11")
                .latitude(10)
                .longitude(106)
                .city("Tân Bình")
                .state("HCM")
                .country("Việt Nam")
                .zipcode("700000")
                .build();

        this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.latitude").value(request.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(request.getLongitude()))
                .andExpect(jsonPath("$.city").value(request.getCity()))
                .andExpect(jsonPath("$.state").value(request.getState()))
                .andExpect(jsonPath("$.country").value(request.getCountry()))
                .andExpect(jsonPath("$.zipcode").value(request.getZipcode()))
                .andReturn();
    }

    @Test
    void update_Successfully() throws Exception {
        final long id = 1;

        LocationRequest request = LocationRequest.builder()
                .address("Test 11 - Updated")
                .latitude(11)
                .longitude(107)
                .city("Tân Bình - Updated")
                .state("HCM - Updated")
                .country("Việt Nam - Updated")
                .zipcode("900000")
                .build();

        this.mockMvc
                .perform(put(ENDPOINT + "/" + id)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.address").value(request.getAddress()))
                .andExpect(jsonPath("$.latitude").value(request.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(request.getLongitude()))
                .andExpect(jsonPath("$.city").value(request.getCity()))
                .andExpect(jsonPath("$.state").value(request.getState()))
                .andExpect(jsonPath("$.country").value(request.getCountry()))
                .andExpect(jsonPath("$.zipcode").value(request.getZipcode()))
                .andReturn();
    }

    @Test
    void delete_Successfully() throws Exception {
        final long id = 1L;
        this.mockMvc
                .perform(delete(ENDPOINT + "/" + id))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data").value("true"))
                .andReturn();
    }
}

