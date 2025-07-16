package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.service.PostCommentService;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class PostCommentController {

    private final PostCommentService postCommentService;

    @PostMapping("/post/{postId}/comment/new")
    public String saveNew(@ModelAttribute PostCommentDto postComment, @PathVariable(name = "postId") Long postId) {
        postCommentService.saveNew(postComment, postId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comment/{commentId}/update")
    public String update(
            @ModelAttribute PostCommentDto postComment,
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") Long commentId) {
        postCommentService.update(postComment, postId, commentId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/comment/{commentId}/delete")
    public String deleteById(@PathVariable(name = "commentId") Long commentId, @PathVariable(name = "postId") Long postId) {
        postCommentService.delete(commentId);
        return "redirect:/post/" + postId;
    }

}
