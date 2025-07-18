package ru.mkryuchkov.blogya.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import ru.mkryuchkov.blogya.HasRecordComponentWithValue;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.entity.PostComment;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostPreviewControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    void findAll_nonNullTag_defaultPaging() throws Exception {
        Long postId1 = postRepository.saveNew(new Post(null, "title", "a", "imageUuid", 0, null, null));
        Long postId2 = postRepository.saveNew(new Post(null, "title2", "body2", "imageUuid2", 0, null, null));
        Long postId3 = postRepository.saveNew(new Post(null, "title3", "body3", "imageUuid3", 0, null, null));

        postTagRepository.rewriteAllTagsForPost(postId1, List.of("tag1", "tag2"));
        postTagRepository.rewriteAllTagsForPost(postId2, List.of("tag1", "tag3"));
        postTagRepository.rewriteAllTagsForPost(postId3, List.of("tag2", "tag3"));

        // post1 has 2 comments
        // post2 has 1 comment
        postCommentRepository.saveNew(new PostComment(null, postId1, "text1", null, null));
        postCommentRepository.saveNew(new PostComment(null, postId1, "text2", null, null));
        postCommentRepository.saveNew(new PostComment(null, postId2, "text3", null, null));

        PageRequest paging = PageRequest.of(0, 10);

        // should return post1 and post2
        mockMvc.perform(get("/posts")
                        .param("tag", "tag1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts",
                        Matchers.hasItems(
                                allOf(
                                        Matchers.isA(PostPreviewDto.class),
                                        HasRecordComponentWithValue.hasRecordComponent("id", notNullValue()),
                                        HasRecordComponentWithValue.hasRecordComponent("title", equalTo("title2")),
                                        HasRecordComponentWithValue.hasRecordComponent("bodyPreview", equalTo("body2")),
                                        HasRecordComponentWithValue.hasRecordComponent("tags", equalTo("tag1, tag3")),
                                        HasRecordComponentWithValue.hasRecordComponent("imageUuid", equalTo("imageUuid2")),
                                        HasRecordComponentWithValue.hasRecordComponent("likes", equalTo(0)),
                                        HasRecordComponentWithValue.hasRecordComponent("commentsCount", equalTo(1)),
                                        HasRecordComponentWithValue.hasRecordComponent("created", notNullValue()),
                                        HasRecordComponentWithValue.hasRecordComponent("updated", notNullValue())
                                ),
                                allOf(
                                        Matchers.isA(PostPreviewDto.class),
                                        HasRecordComponentWithValue.hasRecordComponent("id", notNullValue()),
                                        HasRecordComponentWithValue.hasRecordComponent("title", equalTo("title")),
                                        HasRecordComponentWithValue.hasRecordComponent("bodyPreview", equalTo("a")),
                                        HasRecordComponentWithValue.hasRecordComponent("tags", equalTo("tag1, tag2")),
                                        HasRecordComponentWithValue.hasRecordComponent("imageUuid", equalTo("imageUuid")),
                                        HasRecordComponentWithValue.hasRecordComponent("likes", equalTo(0)),
                                        HasRecordComponentWithValue.hasRecordComponent("commentsCount", equalTo(2)),
                                        HasRecordComponentWithValue.hasRecordComponent("created", notNullValue()),
                                        HasRecordComponentWithValue.hasRecordComponent("updated", notNullValue())
                                ))))
                .andExpect(model().attribute("paging", paging));
    }

    @Test
    void findAll_nullTag() throws Exception {
        Long postId1 = postRepository.saveNew(new Post(null, "title", "a", "imageUuid", 0, null, null));
        Long postId2 = postRepository.saveNew(new Post(null, "title2", "body2", "imageUuid2", 0, null, null));
        Long postId3 = postRepository.saveNew(new Post(null, "title3", "body3", "imageUuid3", 0, null, null));

        postTagRepository.rewriteAllTagsForPost(postId1, List.of("tag1", "tag2"));
        postTagRepository.rewriteAllTagsForPost(postId2, List.of("tag1", "tag3"));
        postTagRepository.rewriteAllTagsForPost(postId3, List.of("tag2", "tag3"));

        // post1 has 2 comments
        // post2 has 1 comment
        postCommentRepository.saveNew(new PostComment(null, postId1, "text1", null, null));
        postCommentRepository.saveNew(new PostComment(null, postId1, "text2", null, null));
        postCommentRepository.saveNew(new PostComment(null, postId2, "text3", null, null));

        int page = 1;
        int pageSize = 2;
        PageRequest paging = PageRequest.of(page, pageSize);

        // should only return post1
        mockMvc.perform(get("/posts")
                        .param("page", "" + page)
                        .param("pageSize", "" + pageSize))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts",
                        allOf(
                                Matchers.iterableWithSize(1),
                                Matchers.hasItem(
                                        allOf(
                                                Matchers.isA(PostPreviewDto.class),
                                                HasRecordComponentWithValue.hasRecordComponent("id", notNullValue()),
                                                HasRecordComponentWithValue.hasRecordComponent("title", equalTo("title")),
                                                HasRecordComponentWithValue.hasRecordComponent("bodyPreview", equalTo("a")),
                                                HasRecordComponentWithValue.hasRecordComponent("tags", equalTo("tag1, tag2")),
                                                HasRecordComponentWithValue.hasRecordComponent("imageUuid", equalTo("imageUuid")),
                                                HasRecordComponentWithValue.hasRecordComponent("likes", equalTo(0)),
                                                HasRecordComponentWithValue.hasRecordComponent("commentsCount", equalTo(2)),
                                                HasRecordComponentWithValue.hasRecordComponent("created", notNullValue()),
                                                HasRecordComponentWithValue.hasRecordComponent("updated", notNullValue())
                                        )))))
                .andExpect(model().attribute("paging", paging));
    }

}
