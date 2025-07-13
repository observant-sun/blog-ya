package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.entity.PostComment;
import ru.mkryuchkov.blogya.mapper.PostCommentMapper;
import ru.mkryuchkov.blogya.repository.PostCommentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostCommentService {

    private final PostCommentRepository postCommentRepository;
    private final PostCommentMapper postCommentMapper;

    public List<PostCommentDto> findAllByPostId(Long postId) {
        List<PostComment> comments = postCommentRepository.findAllByPostId(postId);
        return Optional.ofNullable(comments).orElse(Collections.emptyList())
                .stream()
                .map(postCommentMapper::toDto)
                .toList();
    }

    @Transactional
    public void save(PostCommentDto dto, Long postId) {
        PostComment postComment = postCommentMapper.toEntity(dto, postId);
        if (postComment == null) {
            return;
        }
        if (postComment.id() == null) {
            postCommentRepository.saveNew(postComment);
        } else {
            postCommentRepository.update(postComment);
        }
    }
}
