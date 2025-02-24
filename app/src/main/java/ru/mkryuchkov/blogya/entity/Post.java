package ru.mkryuchkov.blogya.entity;

import java.sql.Timestamp;

public record Post (
    Long id,
    String title,
    String body,
    // TODO: как добавить картинку?
//        String pictureUUID,
    Integer likes,
    Timestamp created,
    Timestamp updated
) {}
