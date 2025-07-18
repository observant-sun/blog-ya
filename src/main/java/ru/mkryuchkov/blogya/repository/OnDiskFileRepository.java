package ru.mkryuchkov.blogya.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ru.mkryuchkov.blogya.entity.FileEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Repository
@Slf4j
@RequiredArgsConstructor
public class OnDiskFileRepository implements FileRepository {

    @Value("${app.files.directory}")
    private String fileDirectory;

    @Override
    public FileEntity saveNewFile(byte[] picture) {
        String uuid = UUID.randomUUID().toString();
        String path = fileDirectory + "/" + uuid;
        createFileDirectory();
        try {
            Files.write(Paths.get(path), picture, StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new FileEntity(uuid, picture);
    }

    @Override
    public Optional<FileEntity> getById(String id) {
        String path = fileDirectory + "/" + id;
        try {
            byte[] content = Files.readAllBytes(Paths.get(path));
            return Optional.of(new FileEntity(id, content));
        } catch (IOException e) {
            log.error("Can't read file", e);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        try (Stream<Path> list = Files.list(Path.of(fileDirectory))) {
            list.forEach(path -> {
                if (Files.isDirectory(path)) {
                    return;
                }
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    log.error("Can't delete file", e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createFileDirectory() {
        Paths.get(fileDirectory).toFile().mkdirs();
    }
}
