package ru.mkryuchkov.blogya.entity;

import java.sql.Timestamp;

public record Post (
    Long id,
    String title,
    String body,
    String imageUuid,
    Integer likes,
    Timestamp created,
    Timestamp updated
) {}
