package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.service.PostPreviewService;

import java.util.List;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostPreviewController {

    private final PostPreviewService postPreviewService;

    @GetMapping
    public String findAll(@RequestParam(required = false, name = "page") Integer page,
                          @RequestParam(required = false, name = "pageSize") Integer pageSize,
                          @RequestParam(required = false, name = "tag") String tag,
                          Model model) {
        page = page == null ? 0 : page;
        pageSize = pageSize == null ? 10 : pageSize;

        List<PostPreviewDto> postPreviewPage = postPreviewService.findAll(tag, page, pageSize);

        model.addAttribute("postPreviews", postPreviewPage);

        return "posts";
    }

}
