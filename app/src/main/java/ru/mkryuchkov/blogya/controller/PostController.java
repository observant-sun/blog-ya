package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.entity.FileEntity;
import ru.mkryuchkov.blogya.service.FileService;
import ru.mkryuchkov.blogya.service.PostService;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final FileService fileService;

    @PostMapping("/save")
    public String save(@RequestParam("image") MultipartFile image, @ModelAttribute PostDto post) {
        Optional<byte[]> bytes = Optional.ofNullable(image).map(multipartFile -> {
            try {
                return multipartFile.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        String imageUuid = null;
        if (bytes.isPresent()) {
            FileEntity fileEntity = fileService.saveNewFile(bytes.get());
            imageUuid = Optional.ofNullable(fileEntity).map(FileEntity::id).orElse(null);
        }
        postService.save(post, imageUuid);
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
