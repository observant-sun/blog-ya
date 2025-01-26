package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.repository.PostCommentRepository;
import ru.mkryuchkov.blogya.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostCommentRepository postCommentRepository;
    private final PostRepository postRepository;

}
