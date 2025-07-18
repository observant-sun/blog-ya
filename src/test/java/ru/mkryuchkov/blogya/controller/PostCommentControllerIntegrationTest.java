package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.entity.PostComment;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostCommentControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    void saveNew() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // save new post
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 123, timestamp, timestamp));
        String commentText = "text123";

        PostCommentDto postCommentDto = new PostCommentDto(null, commentText, timestamp, timestamp);

        mockMvc.perform(post("/post/{postId}/comment/new", postId)
                        .flashAttr("postCommentDto", postCommentDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        List<PostComment> allByPostId = postCommentRepository.findAllByPostId(postId);
        assertEquals(1, allByPostId.size());
        PostComment postComment = allByPostId.getFirst();
        assertEquals(commentText, postComment.text());
    }

    @Test
    void update() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // save new post and comment
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 123, timestamp, timestamp));
        String commentTextOriginal = "text123";
        postCommentRepository.saveNew(new PostComment(null, postId, commentTextOriginal, timestamp, timestamp));
        List<PostComment> allByPostId = postCommentRepository.findAllByPostId(postId);
        Long commentId = allByPostId.getFirst().id();

        String commentTextUpdated = "updatedText";
        PostCommentDto postCommentDto = new PostCommentDto(commentId, commentTextUpdated, timestamp, timestamp);

        mockMvc.perform(post("/post/{postId}/comment/{commentId}/update", postId, commentId)
                        .flashAttr("postCommentDto", postCommentDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        allByPostId = postCommentRepository.findAllByPostId(postId);
        assertEquals(1, allByPostId.size());
        assertEquals(commentTextUpdated, allByPostId.getFirst().text());
    }

    @Test
    void deleteById() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // save new post and comment
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 123, timestamp, timestamp));
        String commentText = "text123";
        String otherCommentText = "otherCommentText";
        postCommentRepository.saveNew(new PostComment(null, postId, commentText, timestamp, timestamp));
        postCommentRepository.saveNew(new PostComment(null, postId, otherCommentText, timestamp, timestamp));
        List<PostComment> allByPostId = postCommentRepository.findAllByPostId(postId);
        assertEquals(2, allByPostId.size());
        allByPostId.sort(Comparator.comparing(PostComment::id));
        Long commentId = allByPostId.getFirst().id();

        mockMvc.perform(post("/post/{postId}/comment/{commentId}/delete", postId, commentId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        // the other comment should be untouched
        allByPostId = postCommentRepository.findAllByPostId(postId);
        assertEquals(1, allByPostId.size());
        assertEquals(otherCommentText, allByPostId.getFirst().text());
    }

}
