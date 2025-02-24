package ru.mkryuchkov.blogya.entity;

import java.sql.Timestamp;

public record PostPreview(
        Long id,
        String title,
        String bodyPreview,
        // TODO: как добавить картинку?
//        String pictureUUID,
        String tags,
        Integer commentsCount,
        Integer likes,
        Timestamp created,
        Timestamp updated
) {
}
