package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.service.PostCommentService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/save")
    public String save(@ModelAttribute PostCommentDto postComment, @RequestParam Long postId) {
        postCommentService.save(postComment, postId);
        return "redirect:/posts";
    }

}
