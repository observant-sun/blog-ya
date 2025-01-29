package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.model.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findAll(Integer limit, Integer offset);
    Optional<Post> findById(Integer id);
    void save(Post post);
    void deleteById(Integer id);

}
