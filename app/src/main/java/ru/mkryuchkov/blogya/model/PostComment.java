package ru.mkryuchkov.blogya.model;

import java.sql.Timestamp;

public record PostComment(
        Integer id,
        Integer postId,
        String text,
        Timestamp created,
        Timestamp updated
) {
}
