package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.entity.PostComment;

import java.util.List;

public interface PostCommentRepository {
    List<PostComment> findAllByPostId(Long postId);

    boolean existsById(Long id);

    void saveNew(PostComment postComment);

    void update(PostComment postComment);

    void deleteById(Long id);

    void deleteAll();
}
