package ru.mkryuchkov.blogya.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.mkryuchkov.blogya.mapper.PostCommentMapper;
import ru.mkryuchkov.blogya.mapper.PostMapper;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.mapper.PostTagMapper;
import ru.mkryuchkov.blogya.repository.*;


//  Сделано именно так, потому что есть требование о максимальном использовании кеширования контекстов.
//  Однако, как мне кажется, написанные так тесты немного сложнее читать и поддерживать.
//  Если бы этого требования не было, я бы написал проще, без наследования.
@SpringBootTest
@ContextConfiguration(classes = ServiceTestConfig.class)
@ActiveProfiles("test")
public abstract class BaseServiceTest {

    @Autowired
    protected FileService fileService;
    @Autowired
    protected PostCommentService postCommentService;
    @Autowired
    protected PostPreviewService postPreviewService;
    @Autowired
    protected PostService postService;

    @MockitoBean
    protected PostCommentRepository postCommentRepository;
    @MockitoBean
    protected PostCommentMapper postCommentMapper;
    @MockitoBean
    protected FileRepository fileRepository;
    @MockitoBean
    protected PostPreviewRepository postPreviewRepository;
    @MockitoBean
    protected PostPreviewMapper postPreviewMapper;
    @MockitoBean
    protected PostMapper postMapper;
    @MockitoBean
    protected PostRepository postRepository;
    @MockitoBean
    protected PostTagMapper postTagMapper;
    @MockitoBean
    protected PostTagRepository postTagRepository;

}
