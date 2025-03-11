package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.Post;
import ru.mkryuchkov.blogya.mapper.PostMapper;
import ru.mkryuchkov.blogya.mapper.PostTagMapper;
import ru.mkryuchkov.blogya.repository.PostRepository;
import ru.mkryuchkov.blogya.repository.PostTagRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;

    private final PostMapper postMapper;
    private final PostTagMapper postTagMapper;

    @Transactional
    public void save(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Long postId = Optional.ofNullable(post).map(Post::id).orElse(null);

        if (postId != null) {
            postRepository.update(post);
        } else {
            postId = postRepository.saveNew(post);
        }

        List<String> tags = postTagMapper.toList(postDto.tags());
        postTagRepository.rewriteAllTagsForPost(postId, tags);
    }

    @Transactional
    public Optional<PostDto> findById(Long id) {
        Optional<Post> postOpt = postRepository.findById(id);
        List<String> tagList = Optional.ofNullable(postTagRepository.getTagsForPost(id)).orElse(List.of());
        String tags = postTagMapper.toCommaDelimitedString(tagList);
        return postOpt.map(post -> postMapper.toDto(post, tags));
    }
}
