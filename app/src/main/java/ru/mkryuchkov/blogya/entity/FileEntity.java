package ru.mkryuchkov.blogya.entity;

public record FileEntity(
        String id,
        byte[] content
) {
}
