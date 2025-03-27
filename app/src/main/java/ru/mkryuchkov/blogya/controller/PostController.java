package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.service.FileService;
import ru.mkryuchkov.blogya.service.PostCommentService;
import ru.mkryuchkov.blogya.service.PostService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final PostCommentService postCommentService;
    private final FileService fileService;

    @PostMapping("/save")
    public String save(@RequestParam("image") MultipartFile image, @ModelAttribute PostDto post) {
        String imageUuid = fileService.saveNewFile(image).map(FileEntity::id).orElse(null);
        postService.save(post, imageUuid);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Long id, Model model) {
        Optional<PostDto> postOpt = postService.findById(id);
        postOpt.ifPresent(postDto -> model.addAttribute("post", postDto));

        List<PostCommentDto> comments = Optional.ofNullable(postCommentService.findAllByPostId(id))
                .orElse(Collections.emptyList());
        model.addAttribute("comments", comments);

        return "post";
    }
}
