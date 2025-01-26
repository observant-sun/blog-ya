package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mkryuchkov.blogya.service.PostCommentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class PostCommentController {

    private final PostCommentService postCommentService;

}
