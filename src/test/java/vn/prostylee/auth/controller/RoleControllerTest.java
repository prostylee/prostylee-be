package vn.prostylee.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.service.RoleService;
import vn.prostylee.core.dto.filter.MasterDataFilter;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    private static final int totalOfRecords = 1;
    private static final int pageSize = 1;
    private MockMvc mockMvc;

    @Mock
    private RoleService roleService;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new RoleController(roleService)).build();
    }

    @Test
    void getAllRoles_Successfully() throws Exception {
        List<RoleResponse> responses = Collections.singletonList(mockResponse());
        Page<RoleResponse> page = new PageImpl<>(responses);
        when(roleService.findAll(new MasterDataFilter())).thenReturn(page);

        MvcResult mvcResult = this.mockMvc
                .perform(get("/v1/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(totalOfRecords))
                .andExpect(jsonPath("$.totalPages").value(totalOfRecords /pageSize))
                .andExpect(jsonPath("$.content.length()").value(pageSize))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    private RoleResponse mockResponse() {
        return RoleResponse.builder()
                .id(1L)
                .code("ADMIN")
                .name("Administrator")
                .build();
    }
}
