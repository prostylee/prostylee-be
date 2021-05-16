package vn.prostylee.auth.controller;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;
import vn.prostylee.auth.dto.request.UserRequest;
import vn.prostylee.auth.dto.response.UserResponse;
import vn.prostylee.auth.service.UserService;
import vn.prostylee.core.provider.AuthenticatedProvider;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.order.service.OrderService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private static final String ENDPOINT = "/v1/users";
    private static final int totalOfRecords = 10;
    private static final int pageSize = 5;

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @Mock
    private AuthenticatedProvider authenticatedProvider;

    @Mock
    private Validator mockValidator;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService, orderService, authenticatedProvider))
                .setValidator(mockValidator) // Need to ignore validation because can not mock a custom validator
                .build();
    }

    @Test
    void getAll_Successfully() throws Exception {
        final int page = 0;
        when(userService.findAll(any())).thenReturn(mockPageResponse(page));

        MvcResult mvcResult = this.mockMvc
                .perform(get(ENDPOINT))
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
        when(userService.findById(userId)).thenReturn(mockResponse(userId));

        MvcResult mvcResult = this.mockMvc
                .perform(get(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(createEmail(userId)))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void create_Successfully() throws Exception {
        final long userId = 1L;
        UserRequest request = UserRequest.builder()
                .username(createEmail(userId))
                .password("1234")
                .build();
        when(userService.save(any(UserRequest.class))).thenReturn(mockResponse(userId));

        MvcResult mvcResult = this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value(createEmail(userId)))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void update_Successfully() throws Exception {
        final long userId = 1L;
        UserRequest request = UserRequest.builder()
                .id(userId)
                .username(createEmail(userId))
                .build();
        when(userService.update(userId, request)).thenReturn(mockResponse(userId));

        MvcResult mvcResult = this.mockMvc
                .perform(put(ENDPOINT + "/" + userId)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value(createEmail(userId)))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
    void delete_Successfully() throws Exception {
        final long userId = 1L;
        when(userService.deleteById(userId)).thenReturn(true);

        MvcResult mvcResult = this.mockMvc
                .perform(delete(ENDPOINT + "/" + userId))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.data").value("true"))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    private Page<UserResponse> mockPageResponse(int page) {
        List<UserResponse> users = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            users.add(mockResponse(i));
        }
        return new PageImpl<>(users, PageRequest.of(page, pageSize, Sort.unsorted()), totalOfRecords);
    }

    private UserResponse mockResponse(long userId) {
        return UserResponse.builder()
                .id(userId)
                .username(createEmail(userId))
                .build();
    }

    private String createEmail(long userId) {
        return StringUtils.join("user", userId, "@prostylee.vn");
    }
}
