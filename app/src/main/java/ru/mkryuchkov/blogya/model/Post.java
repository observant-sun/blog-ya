package ru.mkryuchkov.blogya.model;

import lombok.Builder;

import java.sql.Timestamp;
import java.util.List;

@Builder
public record Post(
        Long id,
        String title,
        String body,
        // TODO: как добавить картинку?
//        String pictureUUID,
        List<String> tags,
        Integer likes,
        Timestamp created,
        Timestamp modified
) {
}
