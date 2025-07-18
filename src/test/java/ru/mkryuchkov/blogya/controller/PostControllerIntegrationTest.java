package ru.mkryuchkov.blogya.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import ru.mkryuchkov.blogya.HasRecordComponentWithValue;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.entity.PostComment;
import ru.mkryuchkov.blogya.entity.PostPreview;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    void editPost() throws Exception {
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 123, null, null));
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));

        mockMvc.perform(get("/post/{postId}/edit", postId))
                .andExpect(status().isOk())
                .andExpect(view().name("add-post"))
                .andExpect(model().attribute("post",
                        allOf(
                                Matchers.isA(PostDto.class),
                                HasRecordComponentWithValue.hasRecordComponent("id", notNullValue()),
                                HasRecordComponentWithValue.hasRecordComponent("title", equalTo("title")),
                                HasRecordComponentWithValue.hasRecordComponent("body", equalTo("body")),
                                HasRecordComponentWithValue.hasRecordComponent("tags", equalTo("tag1, tag2")),
                                HasRecordComponentWithValue.hasRecordComponent("imageUuid", equalTo("imageUuid")),
                                HasRecordComponentWithValue.hasRecordComponent("likes", equalTo(0)),
                                HasRecordComponentWithValue.hasRecordComponent("created", notNullValue()),
                                HasRecordComponentWithValue.hasRecordComponent("updated", notNullValue())
                        )))
                .andExpect(model().hasNoErrors());
    }

    @Test
    void editPost_noSuchPost() throws Exception {
        long postId = 3L;

        mockMvc.perform(get("/post/{postId}/edit", postId))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveNew() throws Exception {
        byte[] content = "content".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);

        mockMvc.perform(multipart("/post/new")
                        .file(mockMultipartFile)
                        .param("title", "text")
                        .param("body", "body")
                        .param("tags", "tags"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        List<PostPreview> posts = postPreviewRepository.findAll(10, 0L);
        assertEquals(1, posts.size());
        PostPreview post = posts.getFirst();
        assertNotNull(post);
        assertEquals("text", post.title());
        assertEquals("body", post.bodyPreview());
        assertEquals("tags", post.tags());
        assertNotNull(post.created());
        assertNotNull(post.updated());
        assertEquals(0, post.likes());
        assertEquals(0, post.commentsCount());
        assertNotNull(post.imageUuid());
        Optional<FileEntity> fileEntityOptional = fileRepository.getById(post.imageUuid());
        assertTrue(fileEntityOptional.isPresent());
        assertArrayEquals(content, fileEntityOptional.get().content());
    }

    @Test
    void update() throws Exception {
        byte[] contentOriginal = "contentOriginal".getBytes();
        FileEntity fileEntity = fileRepository.saveNewFile(contentOriginal);
        String imageUuidOriginal = fileEntity.id();
        Long postId = postRepository.saveNew(new Post(null, "title", "body", imageUuidOriginal, 0, null, null));
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));

        PostDto postDto = new PostDto(postId, "titleUpdated", "bodyUpdated", imageUuidOriginal, "tagsUpdated", 123, null, null);
        byte[] content = "content".getBytes();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);

        mockMvc.perform(multipart("/post/{postId}", postId)
                        .file(mockMultipartFile)
                        .flashAttr("postDto", postDto))
                        .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        Optional<Post> postOpt = postRepository.findById(postId);
        assertTrue(postOpt.isPresent());
        Post post = postOpt.get();
        assertEquals("titleUpdated", post.title());
        assertEquals("bodyUpdated", post.body());
        assertNotEquals(imageUuidOriginal, post.imageUuid());
        assertNotNull(post.updated());
        assertNotNull(post.created());
        assertTrue(post.updated().after(post.created()));
        Optional<FileEntity> fileEntityOptional = fileRepository.getById(post.imageUuid());
        assertTrue(fileEntityOptional.isPresent());
        assertArrayEquals(content, fileEntityOptional.get().content());
        List<String> tagsForPost = postTagRepository.getTagsForPost(postId);
        assertEquals(List.of("tagsUpdated"), tagsForPost);
    }

    @Test
    void update_imageEmpty() throws Exception {
        byte[] contentOriginal = "contentOriginal".getBytes();
        FileEntity fileEntity = fileRepository.saveNewFile(contentOriginal);
        String imageUuidOriginal = fileEntity.id();
        Long postId = postRepository.saveNew(new Post(null, "title", "body", imageUuidOriginal, 0, null, null));
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));

        PostDto postDto = new PostDto(postId, "titleUpdated", "bodyUpdated", imageUuidOriginal, "tagsUpdated", 123, null, null);
        byte[] content = null;
        MockMultipartFile mockMultipartFile = new MockMultipartFile("image", "image.png", MediaType.IMAGE_PNG_VALUE, content);

        mockMvc.perform(multipart("/post/{postId}", postId)
                        .file(mockMultipartFile)
                        .flashAttr("postDto", postDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        Optional<Post> postOpt = postRepository.findById(postId);
        assertTrue(postOpt.isPresent());
        Post post = postOpt.get();
        assertEquals("titleUpdated", post.title());
        assertEquals("bodyUpdated", post.body());
        assertEquals(imageUuidOriginal, post.imageUuid());
        assertNotNull(post.updated());
        assertNotNull(post.created());
        assertTrue(post.updated().after(post.created()));
        Optional<FileEntity> fileEntityOptional = fileRepository.getById(post.imageUuid());
        assertTrue(fileEntityOptional.isPresent());
        assertArrayEquals(contentOriginal, fileEntityOptional.get().content());
        List<String> tagsForPost = postTagRepository.getTagsForPost(postId);
        assertEquals(List.of("tagsUpdated"), tagsForPost);
    }

    @Test
    void update_imageNull() throws Exception {
        byte[] contentOriginal = "contentOriginal".getBytes();
        FileEntity fileEntity = fileRepository.saveNewFile(contentOriginal);
        String imageUuidOriginal = fileEntity.id();
        Long postId = postRepository.saveNew(new Post(null, "title", "body", imageUuidOriginal, 0, null, null));
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));

        PostDto postDto = new PostDto(postId, "titleUpdated", "bodyUpdated", imageUuidOriginal, "tagsUpdated", 123, null, null);

        mockMvc.perform(post("/post/{postId}", postId)
                        .flashAttr("postDto", postDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        Optional<Post> postOpt = postRepository.findById(postId);
        assertTrue(postOpt.isPresent());
        Post post = postOpt.get();
        assertEquals("titleUpdated", post.title());
        assertEquals("bodyUpdated", post.body());
        assertEquals(imageUuidOriginal, post.imageUuid());
        assertNotNull(post.updated());
        assertNotNull(post.created());
        assertTrue(post.updated().after(post.created()));
        Optional<FileEntity> fileEntityOptional = fileRepository.getById(post.imageUuid());
        assertTrue(fileEntityOptional.isPresent());
        assertArrayEquals(contentOriginal, fileEntityOptional.get().content());
        List<String> tagsForPost = postTagRepository.getTagsForPost(postId);
        assertEquals(List.of("tagsUpdated"), tagsForPost);
    }

    @Test
    void show() throws Exception {
        byte[] contentOriginal = "contentOriginal".getBytes();
        FileEntity fileEntity = fileRepository.saveNewFile(contentOriginal);
        String imageUuidOriginal = fileEntity.id();
        Long postId = postRepository.saveNew(new Post(null, "title", "body", imageUuidOriginal, 0, null, null));
        Post post = postRepository.findById(postId).get();
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));
        PostDto postDto = new PostDto(postId, "title", "body", imageUuidOriginal, "tag1, tag2", 0, post.created(), post.updated());
        postCommentRepository.saveNew(new PostComment(null, postId, "comment1", null, null));
        PostComment comment = postCommentRepository.findAllByPostId(postId).getFirst();
        PostCommentDto postCommentDto = new PostCommentDto(comment.id(), comment.text(), comment.created(), comment.updated());

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", postDto))
                .andExpect(model().attribute("comments", List.of(postCommentDto)));
    }

    @Test
    void show_noComments() throws Exception {
        byte[] contentOriginal = "contentOriginal".getBytes();
        FileEntity fileEntity = fileRepository.saveNewFile(contentOriginal);
        String imageUuidOriginal = fileEntity.id();
        Long postId = postRepository.saveNew(new Post(null, "title", "body", imageUuidOriginal, 0, null, null));
        Post post = postRepository.findById(postId).get();
        postTagRepository.rewriteAllTagsForPost(postId, List.of("tag1", "tag2"));
        PostDto postDto = new PostDto(postId, "title", "body", imageUuidOriginal, "tag1, tag2", 0, post.created(), post.updated());

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("post"))
                .andExpect(model().attribute("post", postDto))
                .andExpect(model().attribute("comments", List.of()));
    }

    @Test
    void show_notFound() throws Exception {
        Long postId = 3L;

        mockMvc.perform(get("/post/{postId}", postId))
                .andExpect(status().isNotFound());

    }


    @Test
    void delete() throws Exception {
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 0, null, null));
        postRepository.saveNew(new Post(null, "title2", "body2", "imageUuid2", 0, null, null));
        List<PostPreview> allPosts = postPreviewRepository.findAll(10, 0L);
        assertEquals(2, allPosts.size());

        mockMvc.perform(post("/post/{id}/delete", postId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        allPosts = postPreviewRepository.findAll(10, 0L);
        assertEquals(1, allPosts.size());
    }

    @Test
    void likePost_like() throws Exception {
        Long postId = postRepository.saveNew(new Post(null, "title", "body", "imageUuid", 0, null, null));
        assertEquals(0, postRepository.findById(postId).get().likes());

        mockMvc.perform(post("/post/{id}/like", postId)
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));

        assertEquals(1, postRepository.findById(postId).get().likes());
    }

    @Test
    void likePost_false() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/like", postId)
                        .param("like", "false"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/post/" + postId));
    }

    @Test
    void likePost_null() throws Exception {
        long postId = 3L;

        mockMvc.perform(post("/post/{id}/like", postId))
                .andExpect(status().is4xxClientError());
    }


}
