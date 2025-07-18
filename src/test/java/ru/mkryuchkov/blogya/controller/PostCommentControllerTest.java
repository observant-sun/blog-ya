package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;
import ru.mkryuchkov.blogya.dto.PostCommentDto;

import java.sql.Timestamp;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostCommentControllerTest extends BaseControllerTest {

    @Test
    void saveNew() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long postId = 3L;
        PostCommentDto postCommentDto = new PostCommentDto(4L, "text", timestamp, timestamp);

        mockMvc.perform(post("/post/{postId}/comment/new", postId)
                        .flashAttr("postCommentDto", postCommentDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(postCommentService).saveNew(postCommentDto, postId);
    }

    @Test
    void update() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long postId = 3L;
        long commentId = 4L;
        PostCommentDto postCommentDto = new PostCommentDto(commentId, "text", timestamp, timestamp);

        mockMvc.perform(post("/post/{postId}/comment/{commentId}/update", postId, commentId)
                        .flashAttr("postCommentDto", postCommentDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(postCommentService).update(postCommentDto, postId, commentId);
    }

    @Test
    void deleteById() throws Exception {
        long postId = 3L;
        long commentId = 4L;

        mockMvc.perform(post("/post/{postId}/comment/{commentId}/delete", postId, commentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(postCommentService).delete(commentId);
    }

}
