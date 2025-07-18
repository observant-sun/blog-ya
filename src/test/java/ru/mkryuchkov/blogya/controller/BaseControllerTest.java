package ru.mkryuchkov.blogya.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.mkryuchkov.blogya.service.FileService;
import ru.mkryuchkov.blogya.service.PostCommentService;
import ru.mkryuchkov.blogya.service.PostPreviewService;
import ru.mkryuchkov.blogya.service.PostService;

@WebMvcTest
@ContextConfiguration(classes = ControllerTestConfig.class)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected FileService fileService;
    @MockitoBean
    protected PostCommentService postCommentService;
    @MockitoBean
    protected PostPreviewService postPreviewService;
    @MockitoBean
    protected PostService postService;
}
