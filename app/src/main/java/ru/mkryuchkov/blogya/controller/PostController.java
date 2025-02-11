package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mkryuchkov.blogya.model.Post;
import ru.mkryuchkov.blogya.service.PostService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/new")
    public String saveNew(@ModelAttribute Post post) {
        postService.saveNew(post);

        return "redirect:/posts";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute Post post) {
        postService.update(post);

        return "redirect:/posts";
    }
}
