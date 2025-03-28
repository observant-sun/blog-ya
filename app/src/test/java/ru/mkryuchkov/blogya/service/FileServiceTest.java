package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.ServiceTestConfig;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.repository.FileRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
public class FileServiceTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

    @BeforeEach
    void resetMocks() {
        reset(fileRepository);
    }

    @Test
    void saveNewFile_byteArray() {
        byte[] content = {1, 2, 3};
        FileEntity fileEntity = new FileEntity("10", content);
        doReturn(fileEntity).when(fileRepository).saveNewFile(content);

        Optional<FileEntity> actual = fileService.saveNewFile(content);
        verify(fileRepository).saveNewFile(content);

        assertTrue(actual.isPresent());
        assertEquals(fileEntity, actual.get());
    }

    @Test
    void saveNewFile_multipartFile() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        byte[] content = {1, 2, 3};
        FileEntity fileEntity = new FileEntity("10", content);

        doReturn(fileEntity).when(fileRepository).saveNewFile(content);
        doReturn(content).when(multipartFile).getBytes();

        Optional<FileEntity> actual = fileService.saveNewFile(multipartFile);
        verify(fileRepository).saveNewFile(content);
        verify(multipartFile).getBytes();

        assertTrue(actual.isPresent());
        assertEquals(fileEntity, actual.get());
    }

    @Test
    void findById() {
        byte[] content = {1, 2, 3};
        FileEntity fileEntity = new FileEntity("10", content);
        doReturn(Optional.of(fileEntity)).when(fileRepository).getById("10");

        Optional<byte[]> actual = fileService.findById("10");
        verify(fileRepository).getById("10");
        assertTrue(actual.isPresent());
        assertArrayEquals(content, actual.get());
    }
}