package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.FileEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostControllerTest extends BaseControllerTest {

    @Test
    void newPost() throws Exception {
        mockMvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("add-post"));
    }

    @Test
    void editPost() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long postId = 3L;
        PostDto postDto = new PostDto(postId, "text", "body", "imageUuid", "tags", 123, timestamp, timestamp);

        when(postService.findById(postId)).thenReturn(Optional.of(postDto));

        mockMvc.perform(get("/post/{postId}/edit", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post", postDto))
                .andExpect(model().hasNoErrors());

        verify(postService).findById(postId);
    }

    @Test
    void editPost_noSuchPost() throws Exception {
        long postId = 3L;

        when(postService.findById(postId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/post/{postId}/edit", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"));

        verify(postService).findById(postId);
    }

    @Test
    void saveNew() throws Exception {
        PostDto postDto = new PostDto(null, "text", "body", null, "tags", null, null, null);
        String imageUuid = "imageUuid1";
        byte[] content = "content".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);

        when(fileService.saveNewFile(mockMultipartFile)).thenReturn(Optional.of(new FileEntity(imageUuid, content)));

        mockMvc.perform(multipart("/post/new")
                        .file(mockMultipartFile)
                        .param("title", "text")
                        .param("body", "body")
                        .param("tags", "tags"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(fileService).saveNewFile(mockMultipartFile);
        verify(postService).saveNew(postDto, imageUuid);
    }

    @Test
    void update() throws Exception {
        Long postId = 3L;
        String imageUuidOriginal = "imageUuidOriginal";
        String imageUuidNew = "imageUuidNew";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostDto postDto = new PostDto(postId, "text", "body", imageUuidOriginal, "tags", 123, timestamp, timestamp);
        byte[] content = "content".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);


        when(fileService.saveNewFile(mockMultipartFile)).thenReturn(Optional.of(new FileEntity(imageUuidNew, content)));
        mockMvc.perform(multipart("/post/{postId}", postId)
                        .file(mockMultipartFile)
                        .flashAttr("postDto", postDto))
                        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(fileService).saveNewFile(mockMultipartFile);
        verify(postService).update(postDto, postId, imageUuidNew);
    }

    @Test
    void update_imageEmpty() throws Exception {
        Long postId = 3L;
        String imageUuidOriginal = "imageUuidOriginal";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostDto postDto = new PostDto(postId, "text", "body", imageUuidOriginal, "tags", 123, timestamp, timestamp);
        byte[] content = null;
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);

        mockMvc.perform(multipart("/post/{postId}", postId)
                        .file(mockMultipartFile)
                        .flashAttr("postDto", postDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(fileService, never()).saveNewFile(any(MultipartFile.class));
        verify(postService).update(postDto, postId, null);
    }

    @Test
    void update_imageNull() throws Exception {
        Long postId = 3L;
        String imageUuidOriginal = "imageUuidOriginal";
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostDto postDto = new PostDto(postId, "text", "body", imageUuidOriginal, "tags", 123, timestamp, timestamp);

        mockMvc.perform(post("/post/{postId}", postId)
                        .flashAttr("postDto", postDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(fileService, never()).saveNewFile(any(MultipartFile.class));
        verify(postService).update(postDto, postId, null);
    }

    @Test
    void show() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long postId = 3L;
        PostDto postDto = new PostDto(postId, "text", "body", "imageUuid", "tags", 123, timestamp, timestamp);
        PostCommentDto postCommentDto = new PostCommentDto(1L, "text1", timestamp, timestamp);

        when(postService.findById(postId)).thenReturn(Optional.of(postDto));
        when(postCommentService.findAllByPostId(postId)).thenReturn(List.of(postCommentDto));

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", postDto))
                .andExpect(model().attribute("comments", List.of(postCommentDto)));

        verify(postService).findById(postId);
        verify(postCommentService).findAllByPostId(postId);
    }

    @Test
    void show_nullComments() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long postId = 3L;
        PostDto postDto = new PostDto(postId, "text", "body", "imageUuid", "tags", 123, timestamp, timestamp);
        PostCommentDto postCommentDto = new PostCommentDto(1L, "text1", timestamp, timestamp);

        when(postService.findById(postId)).thenReturn(Optional.of(postDto));
        when(postCommentService.findAllByPostId(postId)).thenReturn(null);

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", postDto))
                .andExpect(model().attribute("comments", List.of()));

        verify(postService).findById(postId);
        verify(postCommentService).findAllByPostId(postId);
    }

    @Test
    void show_notFound() throws Exception {
        long postId = 3L;

        when(postService.findById(postId)).thenReturn(Optional.empty());
        when(postCommentService.findAllByPostId(postId)).thenReturn(List.of());

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isNotFound());

        verify(postService).findById(postId);
    }


    @Test
    void delete() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/delete", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        verify(postService).deleteById(postId);
    }

    @Test
    void likePost_like() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/like", postId)
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(postService).likePost(postId, true);
    }

    @Test
    void likePost_false() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/like", postId)
                        .param("like", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        verify(postService).likePost(postId, false);
    }

    @Test
    void likePost_null() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/like", postId))
                .andExpect(status().is4xxClientError());
    }


}
