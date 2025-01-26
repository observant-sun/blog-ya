package ru.mkryuchkov.blogya.model;

import java.sql.Timestamp;

public record PostComment(
        Long id,
        Long postId,
        String text,
        Timestamp created,
        Timestamp modified
) {
}
