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

    @GetMapping("/new")
    public String newPost() {
        return "add-post";
    }

    @GetMapping("/{id}/edit")
    public String editPost(@PathVariable(name = "id") Long id, Model model) {
        Optional<PostDto> postOpt = postService.findById(id);
        postOpt.ifPresent(postDto -> model.addAttribute("post", postDto));

        return "add-post";
    }

    @PostMapping("/new")
    public String saveNew(@RequestParam("image") MultipartFile image, @ModelAttribute PostDto post) {
        String imageUuid = fileService.saveNewFile(image).map(FileEntity::id).orElse(null);
        postService.saveNew(post, imageUuid);
        return "redirect:/posts";
    }

    @PostMapping("/{id}")
    public String update(@RequestParam(value = "image", required = false) MultipartFile image,
                         @PathVariable(name = "id") Long id,
                         @ModelAttribute PostDto post) {
        String imageUuid = null;
        if (image != null) {
            imageUuid = fileService.saveNewFile(image).map(FileEntity::id).orElse(null);
        }
        postService.update(post, id, imageUuid);
        return "redirect:/post/" + id;
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

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") Long id) {
        postService.deleteById(id);
        return "redirect:/posts";
    }

    @PostMapping("/{id}/like")
    public String likePost(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = "like") Boolean like) {
        postService.likePost(id, like);

        return "redirect:/post/{id}";
    }

}
