package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.entity.Post;

import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Long id);

    Long saveNew(Post post);

    void updateText(Post post);

    void updateImage(Long postId, String imageUuid);

    boolean existsById(Long id);

    void deleteById(Long id);

    void incrementLikes(Long id);

    void decrementLikes(Long id);

    void deleteAll();
}
