package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.entity.Post;

import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Long id);

    Long saveNew(Post post);

    void update(Post post);

    boolean existsById(Long id);

    void deleteById(Long id);

}
