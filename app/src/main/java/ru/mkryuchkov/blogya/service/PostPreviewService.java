package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostPreviewService {

    private final PostPreviewRepository postPreviewRepository;

    private final PostPreviewMapper postPreviewMapper;

    public List<PostPreviewDto> getPage(String tag, Pageable pageable) {
        List<PostPreview> posts;
        if (tag == null) {
            posts = postPreviewRepository.findAll(pageable.getPageSize(), pageable.getOffset());
        } else {
            tag = tag.trim();
            posts = postPreviewRepository.findAllByTag(tag, pageable.getPageSize(), pageable.getOffset());
        }
        return Optional.ofNullable(posts).orElse(Collections.emptyList())
                .stream()
                .map(postPreviewMapper::toDto)
                .collect(Collectors.toList());
    }

}
