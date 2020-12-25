package vn.prostylee.comment.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import vn.prostylee.comment.constant.CommentDestinationType;
import vn.prostylee.comment.dto.response.CommentResponse;
import vn.prostylee.comment.entity.CommentImage;
import vn.prostylee.comment.service.impl.CommentServiceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CommentControllerTest {
    private static final String ENDPOINT = "/v1/comments";
    private static final int totalOfRecords = 10;
    private static final int pageSize = 5;

    private MockMvc mockMvc;

    @Mock
    private CommentServiceImpl service;

    @Test
    void getAll_Successfully() throws Exception {
        final int page = 0;
        when(service.findAll(any())).thenReturn(mockPageResponse(page));

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

    private Page<CommentResponse> mockPageResponse(int page) {
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (int i = 0; i < pageSize; i++) {
            commentResponses.add(mockResponse(i));
        }
        return new PageImpl<>(commentResponses, PageRequest.of(page, pageSize, Sort.unsorted()), totalOfRecords);
    }

    private CommentResponse mockResponse(long id) {
        CommentResponse response = new CommentResponse();
        response.setId(id);
        response.setContent("This is content");
        response.setTargetType(CommentDestinationType.STORE.getStatus());
        response.setCommentImages(mockResponseCommentImage());
        return  response;
    }
    private Set<CommentImage> mockResponseCommentImage() {
        Set<CommentImage> images =  new HashSet<>();
        CommentImage item =  new CommentImage();
        item.setId(Long.valueOf(120921039));
        item.setAttachmentId(Long.valueOf(112323));
        item.setOrder(1);
        images.add(item);
        return images;
    }

}
