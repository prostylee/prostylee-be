package vn.prostylee.post.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import vn.prostylee.AuthSupporterIT;
import vn.prostylee.IntegrationTest;
import vn.prostylee.core.utils.JsonUtils;
import vn.prostylee.post.dto.request.PostImageRequest;
import vn.prostylee.post.dto.request.PostRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void create_Successfully() throws Exception {
        super.setAuth();
        List<PostImageRequest> postImageRequests = new ArrayList<>();
        postImageRequests.add(PostImageRequest.builder().name("prostylee1.jpg").path("abc/test/").build());
        PostRequest request = PostRequest.builder()
                .description("Add new store")
                .storeId(1L)
                .postImageRequests(postImageRequests)
                .build();
        MvcResult mvcResult = this.mockMvc
                .perform(post(ENDPOINT)
                        .content(JsonUtils.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andReturn();
        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
    }

    @Test
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

    @Test
    void update_AddNewPhotos_RemoveOne_Successfully() throws Exception {
        List<PostImageRequest> postImageRequests = new ArrayList<>();
        postImageRequests.add(PostImageRequest.builder().name("prostylee1.jpg").path("abc/test1/").build());
        postImageRequests.add(PostImageRequest.builder().name("prostylee2.jpg").path("abc/test2/").build());
        final long id = 1;
        PostRequest request = PostRequest.builder()
                .description("Updated store 10")
                .storeId(1L).postImageRequests(postImageRequests).attachmentDeleteIds(Arrays.asList(101L))
                .build();
        this.mockMvc.perform(put(ENDPOINT + "/" + id)
                .content(JsonUtils.toJson(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.postImages[0].attachmentId").value(102))
                .andExpect(jsonPath("$.postImages[1].attachmentId").value(103))
                .andReturn();
    }

}
