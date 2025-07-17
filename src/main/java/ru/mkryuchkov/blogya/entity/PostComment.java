package ru.mkryuchkov.blogya.entity;

import java.sql.Timestamp;

public record PostComment(
        Long id,
        Long postId,
        String text,
        Timestamp created,
        Timestamp updated
) {
}
