package ru.mkryuchkov.blogya.dto;

import java.sql.Timestamp;

public record PostCommentDto(
        Long id,
        String text,
        Timestamp created,
        Timestamp updated
) {
}
