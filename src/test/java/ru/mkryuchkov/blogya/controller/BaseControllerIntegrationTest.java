package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.mkryuchkov.blogya.repository.*;

import java.nio.file.Paths;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class BaseControllerIntegrationTest {

    @Value("${app.files.directory}")
    protected String fileDirectory;

    @Autowired
    protected FileRepository fileRepository;
    @Autowired
    protected PostCommentRepository postCommentRepository;
    @Autowired
    protected PostPreviewRepository postPreviewRepository;
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected PostTagRepository postTagRepository;

    @BeforeEach
    public void setUp() {
        Paths.get(fileDirectory).toFile().mkdirs();
        fileRepository.deleteAll();
        postCommentRepository.deleteAll();
        postRepository.deleteAll();
        postTagRepository.deleteAll();
    }

    @Autowired
    protected MockMvc mockMvc;

}
