package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.service.PostService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping("/save")
    public String save(@ModelAttribute PostDto post) {
        postService.save(post);

        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, Model model) {
        Optional<PostDto> postOpt = postService.findById(id);
        if (postOpt.isEmpty()) {
            return ""; // TODO ?
        }
        model.addAttribute("post", postOpt.get());

        return "post";
    }
}
