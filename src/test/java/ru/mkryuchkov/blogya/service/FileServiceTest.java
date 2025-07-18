package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.repository.FileRepository;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = FileService.class)
public class FileServiceTest {

    @MockitoBean
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;

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
    void saveNewFile_multipartFile_getBytesThrowsIOException() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(multipartFile.getBytes()).thenThrow(new IOException("IO exception"));

        assertThrows(RuntimeException.class, () -> fileService.saveNewFile(multipartFile));
        verify(multipartFile).getBytes();
        verifyNoMoreInteractions(multipartFile, fileRepository);
    }

    @Test
    void saveNewFile_multipartFile_getBytesReturnsNull() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(multipartFile.getBytes()).thenReturn(null);

        Optional<FileEntity> actual = fileService.saveNewFile(multipartFile);
        verify(multipartFile).getBytes();
        verifyNoMoreInteractions(multipartFile, fileRepository);

        assertFalse(actual.isPresent());
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