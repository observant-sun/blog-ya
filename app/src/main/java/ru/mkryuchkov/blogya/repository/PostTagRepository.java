package ru.mkryuchkov.blogya.repository;

import java.util.List;

public interface PostTagRepository {

    void rewriteAllTagsForPost(Long postId, List<String> tags);

    List<String> getTagsForPost(Long postId);
}
