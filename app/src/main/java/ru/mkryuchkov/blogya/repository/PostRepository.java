package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.model.Post;

import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Integer id);
    void saveNew(Post post);
    void update(Post post);
    boolean existsById(Integer id);
    void deleteById(Integer id);

}
