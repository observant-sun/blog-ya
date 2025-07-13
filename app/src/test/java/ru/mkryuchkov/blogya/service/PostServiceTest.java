package ru.mkryuchkov.blogya.service;

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

    @Test
    void save() {
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

        postService.save(postDto, imageUuid);

        verify(postMapper).toEntity(postDto, imageUuid);
        verify(postRepository).saveNew(post);
        verify(postTagMapper).toList(tagString);
        verify(postTagRepository).rewriteAllTagsForPost(newPostId, tags);
    }
}
