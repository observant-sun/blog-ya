package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.repository.FileRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public FileEntity saveNewFile(byte[] content) {
        return fileRepository.saveNewFile(content);
    }

    public Optional<byte[]> findById(String id) {
        return fileRepository.getById(id).map(FileEntity::content);
    }
}
