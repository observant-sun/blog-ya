package ru.mkryuchkov.blogya.entity;

import java.sql.Timestamp;
import java.util.List;

public record PostPreview(
        Long id,
        String title,
        String bodyPreview,
        String imageUuid,
        String tags,
        Integer commentsCount,
        Integer likes,
        Timestamp created,
        Timestamp updated
) {
}
