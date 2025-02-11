package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.model.Post;
import ru.mkryuchkov.blogya.repository.PostCommentRepository;
import ru.mkryuchkov.blogya.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

    public void saveNew(Post post) {
        if (post.id() != null) {
            throw new RuntimeException("Post id is already set");
        }
        postRepository.saveNew(post);
    }

    public void update(Post post) {
        if (post.id() == null) {
            throw new RuntimeException("Post id is unset");
        }
        postRepository.update(post);
    }
}
