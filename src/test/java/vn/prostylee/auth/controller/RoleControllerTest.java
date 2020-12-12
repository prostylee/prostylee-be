package vn.prostylee.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import vn.prostylee.auth.dto.response.RoleResponse;
import vn.prostylee.auth.service.RoleService;

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
        when(roleService.findAll()).thenReturn(responses);

        MvcResult mvcResult = this.mockMvc
                .perform(get("/v1/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(1L))
                .andExpect(jsonPath("[0].code").value("ADMIN"))
                .andExpect(jsonPath("[0].name").value("Administrator"))
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
