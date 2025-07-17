package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.entity.PostPreview;

import java.util.List;

public interface PostPreviewRepository {

    List<PostPreview> findAll(Integer limit, Long offset);

    List<PostPreview> findAllByTag(String tag, Integer limit, Long offset);

}
