package ru.mkryuchkov.blogya.dto;

import java.sql.Timestamp;

public record PostPreviewDto(
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
