package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.repository.FileRepository;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public Optional<FileEntity> saveNewFile(byte[] content) {
        return Optional.ofNullable(fileRepository.saveNewFile(content));
    }

    public Optional<FileEntity> saveNewFile(MultipartFile multipartFile) {
        Optional<byte[]> bytes = Optional.ofNullable(multipartFile).map(mpf -> {
            try {
                return mpf.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        if (bytes.isPresent()) {
            return saveNewFile(bytes.get());
        }
        return Optional.empty();
    }

    public Optional<byte[]> findById(String id) {
        return fileRepository.getById(id).map(FileEntity::content);
    }
}
