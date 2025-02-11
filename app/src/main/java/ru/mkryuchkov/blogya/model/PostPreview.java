package ru.mkryuchkov.blogya.model;

import java.sql.Timestamp;
import java.util.List;

public record PostPreview(
        Integer id,
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
