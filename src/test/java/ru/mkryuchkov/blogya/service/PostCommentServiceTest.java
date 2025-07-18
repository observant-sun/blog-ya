package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.Test;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.entity.PostComment;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PostCommentServiceTest extends BaseServiceTest {

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
    void saveNew() {
        Timestamp timestamp = new Timestamp(414141441L);
        long postId = 3L;
        PostComment postComment1 = new PostComment(null, postId, "text", timestamp, timestamp);
        PostCommentDto postCommentDto1 = new PostCommentDto(null, "text", timestamp, timestamp);


        when(postCommentMapper.toEntity(postCommentDto1, postId)).thenReturn(postComment1);

        postCommentService.saveNew(postCommentDto1, postId);

        verify(postCommentMapper).toEntity(postCommentDto1, postId);
        verify(postCommentRepository).saveNew(postComment1);
        verifyNoMoreInteractions(postCommentRepository, postCommentMapper);
    }

    @Test
    void update() {
        Timestamp timestamp = new Timestamp(414141441L);
        long postId = 3L;
        long commentId = 4L;
        PostComment postComment1 = new PostComment(commentId, postId, "text", timestamp, timestamp);
        PostCommentDto postCommentDto1 = new PostCommentDto(1L, "text", timestamp, timestamp);


        when(postCommentMapper.toEntity(postCommentDto1, postId, commentId)).thenReturn(postComment1);

        postCommentService.update(postCommentDto1, postId, commentId);

        verify(postCommentMapper).toEntity(postCommentDto1, postId, commentId);
        verify(postCommentRepository).update(postComment1);
        verifyNoMoreInteractions(postCommentRepository, postCommentMapper);
    }

    @Test
    void delete() {
        long commentId = 1L;
        postCommentService.delete(commentId);

        verify(postCommentRepository).deleteById(commentId);
        verifyNoMoreInteractions(postCommentRepository, postCommentMapper);
    }
}