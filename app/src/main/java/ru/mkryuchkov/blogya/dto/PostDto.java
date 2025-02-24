package ru.mkryuchkov.blogya.dto;

import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record PostDto(
        Long id,
        String title,
        String body,
        // TODO: как добавить картинку?
//        String pictureUUID,
        String tags,
        Integer likes,
        Timestamp created,
        Timestamp updated
) {
}
