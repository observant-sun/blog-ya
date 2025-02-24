package ru.mkryuchkov.blogya.dto;

import java.sql.Timestamp;

public record PostPreviewDto(
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
