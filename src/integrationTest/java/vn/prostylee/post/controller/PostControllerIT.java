package vn.prostylee.post.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.AuthSupporterIT;
import vn.prostylee.IntegrationTest;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.media.dto.request.MediaRequest;
import vn.prostylee.post.dto.request.PostRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@IntegrationTest
@WebAppConfiguration
public class PostControllerIT extends AuthSupporterIT {
    private static final String ENDPOINT = "/v1/posts";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @Order(1)
    void create_Successfully() throws Exception {
        super.setAuth();
        List<MediaRequest> images = new ArrayList<>();
        images.add(MediaRequest.builder().name("prostylee1.jpg").path("abc/test/").build());
        PostRequest request = PostRequest.builder()
                .description("Add new store")
                .storeId(1L)
                .images(images)
                .build();

        this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andReturn();
    }

    @Test
    @Order(2)
    void update_Successfully() throws Exception {
        final long id = 1;
        PostRequest request = PostRequest.builder()
                .description("Updated store 10")
                .storeId(1L)
                .build();
        this.mockMvc
                .perform(put(ENDPOINT + "/" + id)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andReturn();
    }

    @Disabled //TODO: Fix IT
    @Test
    @Order(3)
    void update_AddNewPhotos_RemoveOne_Successfully() throws Exception {
        List<MediaRequest> images = new ArrayList<>();
        images.add(MediaRequest.builder().name("prostylee1.jpg").path("abc/test1/").build());
        images.add(MediaRequest.builder().name("prostylee2.jpg").path("abc/test2/").build());

        final long id = 1L;
        PostRequest request = PostRequest.builder()
                .description("Updated store 10")
                .storeId(1L)
                .images(images)
                .attachmentDeleteIds(Collections.singletonList(1L))
                .build();

        this.mockMvc.perform(put(ENDPOINT + "/" + id)
                .content(JsonUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.postImages[0].attachmentId").value(2))
                .andExpect(jsonPath("$.postImages[1].attachmentId").value(3))
                .andReturn();
    }

}
