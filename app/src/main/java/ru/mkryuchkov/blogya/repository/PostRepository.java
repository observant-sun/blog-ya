package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.model.Post;

import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Integer id);
    void save(Post post);
    void deleteById(Integer id);

}
