package ru.mkryuchkov.blogya.repository;

import ru.mkryuchkov.blogya.entity.FileEntity;

import java.util.Optional;

public interface FileRepository {

    FileEntity saveNewFile(byte[] picture);

    Optional<FileEntity> getById(String id);

}
