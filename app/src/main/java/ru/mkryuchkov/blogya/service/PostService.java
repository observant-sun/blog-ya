package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.mapper.PostMapper;
import ru.mkryuchkov.blogya.repository.PostCommentRepository;
import ru.mkryuchkov.blogya.repository.PostRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public void saveNew(PostDto postDto) {
        if (postDto.id() != null) {
            throw new RuntimeException("PostDto id is already set");
        }
        Post post = postMapper.toEntity(postDto);
        postRepository.saveNew(post);
    }

    public void update(PostDto postDto) {
        if (postDto.id() == null) {
            throw new RuntimeException("PostDto id is unset");
        }
        Post post = postMapper.toEntity(postDto);
        postRepository.update(post);
    }

    public Optional<PostDto> findById(Long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        return postOpt.map(post -> postMapper.toDto(post, "")); // TODO
    }
}
