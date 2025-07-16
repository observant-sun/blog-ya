package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public String findAll(@RequestParam(required = false, name = "page", defaultValue = "0") Integer page,
                          @RequestParam(required = false, name = "pageSize", defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false, name = "tag") String tag,
                          Model model) {
        Pageable pageable = PageRequest.of(page, pageSize);
        List<PostPreviewDto> postPreviewPage = postPreviewService.getPage(tag, pageable);

        model.addAttribute("postPreviews", postPreviewPage);
        // TODO: does it work?
        model.addAttribute("paging", pageable);

        return "posts";
    }

}
