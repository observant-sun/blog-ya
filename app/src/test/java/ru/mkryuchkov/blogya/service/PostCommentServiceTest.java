package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mkryuchkov.blogya.ServiceTestConfig;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.entity.PostComment;
import ru.mkryuchkov.blogya.mapper.PostCommentMapper;
import ru.mkryuchkov.blogya.repository.PostCommentRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static java.time.LocalTime.now;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
class PostCommentServiceTest {

    @Autowired
    private PostCommentService postCommentService;

    @Autowired
    private PostCommentRepository postCommentRepository;
    @Autowired
    private PostCommentMapper postCommentMapper;

    @Test
    void findAllByPostId() {
        Timestamp timestamp = new Timestamp(414141441L);
        PostComment postComment1 = new PostComment(1L, 1L, "text", timestamp, timestamp);
        PostComment postComment2 = new PostComment(2L, 1L, "text2", timestamp, timestamp);
        PostCommentDto postCommentDto1 = new PostCommentDto(1L, "text", timestamp, timestamp);
        PostCommentDto postCommentDto2 = new PostCommentDto(2L, "text2", timestamp, timestamp);

        doReturn(List.of(postComment1, postComment2)).when(postCommentRepository).findAllByPostId(1L);
        doReturn(postCommentDto1).when(postCommentMapper).toDto(postComment1);
        doReturn(postCommentDto2).when(postCommentMapper).toDto(postComment2);

        List<PostCommentDto> actual = postCommentService.findAllByPostId(1L);
        verify(postCommentRepository).findAllByPostId(1L);
        verify(postCommentMapper).toDto(postComment1);
        verify(postCommentMapper).toDto(postComment2);

        assertEquals(List.of(postCommentDto1, postCommentDto2), actual);
    }

    @Test
    void save() {
    }
}