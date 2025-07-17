package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mkryuchkov.blogya.ServiceTestConfig;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.mapper.PostMapper;
import ru.mkryuchkov.blogya.mapper.PostTagMapper;
import ru.mkryuchkov.blogya.repository.PostRepository;
import ru.mkryuchkov.blogya.repository.PostTagRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class PostServiceTest {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private PostService postService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostTagMapper postTagMapper;
    @Autowired
    private PostTagRepository postTagRepository;

    @BeforeEach
    public void resetMocks() {
        reset(postMapper, postTagMapper, postRepository, postTagRepository);
    }

    private void verifyNoMoreMockInteractions() {
        verifyNoMoreInteractions(postMapper, postTagRepository, postRepository, postTagMapper);
    }

    @Test
    void saveNew() {
        Timestamp timestamp = new Timestamp(414141441L);
        String tagString = "tag1,tag2";
        PostDto postDto = new PostDto(null, "title1", "body1", null, tagString, 123, timestamp, timestamp);
        String imageUuid = UUID.randomUUID().toString();
        Post post = new Post(null, "title1", "body1", imageUuid, 123, timestamp, timestamp);
        Long newPostId = 1231L;

        when(postMapper.toEntity(postDto, imageUuid)).thenReturn(post);
        when(postRepository.saveNew(post)).thenReturn(newPostId);
        List<String> tags = List.of("tag1", "tag2");
        when(postTagMapper.toList(tagString)).thenReturn(tags);

        postService.saveNew(postDto, imageUuid);

        verify(postMapper).toEntity(postDto, imageUuid);
        verify(postRepository).saveNew(post);
        verify(postTagMapper).toList(tagString);
        verify(postTagRepository).rewriteAllTagsForPost(newPostId, tags);
        verifyNoMoreMockInteractions();
    }

    @Test
    void update() {
        Timestamp timestamp = new Timestamp(414141441L);
        String tagString = "tag1,tag2";
        Long postId = 1231L;
        PostDto postDto = new PostDto(postId, "title1", "body1", null, tagString, 123, timestamp, timestamp);
        String imageUuid = UUID.randomUUID().toString();
        Post post = new Post(postId, "title1", "body1", imageUuid, 123, timestamp, timestamp);
        Long newPostId = 1231L;

        when(postMapper.toEntity(postDto, postId, imageUuid)).thenReturn(post);
        List<String> tags = List.of("tag1", "tag2");
        when(postTagMapper.toList(tagString)).thenReturn(tags);

        postService.update(postDto, postId, imageUuid);

        verify(postMapper).toEntity(postDto, postId, imageUuid);
        verify(postRepository).updateText(post);
        verify(postRepository).updateImage(postId, imageUuid);
        verify(postTagMapper).toList(tagString);
        verify(postTagRepository).rewriteAllTagsForPost(newPostId, tags);
        verifyNoMoreMockInteractions();
    }

    @Test
    void deleteById() {
        Long postId = 1231L;

        postService.deleteById(postId);

        verify(postRepository).deleteById(postId);
        verify(postTagRepository).deleteAllTagsForPost(postId);
        verifyNoMoreMockInteractions();
    }

    @Test
    void like_true() {
        Long postId = 1231L;

        postService.likePost(postId, true);

        verify(postRepository).incrementLikes(postId);
        verifyNoMoreMockInteractions();
    }

    @Test
    void like_false() {
        Long postId = 1231L;

        postService.likePost(postId, false);

        verify(postRepository).decrementLikes(postId);
        verifyNoMoreMockInteractions();
    }

    @Test
    void like_null() {
        Long postId = 1231L;

        postService.likePost(postId, null);

        verify(postRepository).decrementLikes(postId);
        verifyNoMoreMockInteractions();
    }

    @Test
    void findById() {
        Timestamp timestamp = new Timestamp(414141441L);
        Long postId = 1231L;
        String imageUuid = UUID.randomUUID().toString();
        Post post = new Post(postId, "title1", "body1", imageUuid, 123, timestamp, timestamp);
        List<String> tagList = List.of("tag1", "tag2");
        String tags = "tag1,tag2";
        PostDto postDto = new PostDto(postId, "title1", "body1", imageUuid, tags, 123, timestamp, timestamp);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postTagRepository.getTagsForPost(postId)).thenReturn(tagList);
        when(postTagMapper.toCommaDelimitedString(tagList)).thenReturn(tags);
        when(postMapper.toDto(post, tags)).thenReturn(postDto);

        postService.findById(postId);

        verify(postRepository).findById(postId);
        verify(postTagRepository).getTagsForPost(postId);
        verify(postTagMapper).toCommaDelimitedString(tagList);
        verify(postMapper).toDto(post, tags);
        verifyNoMoreMockInteractions();
    }
}
