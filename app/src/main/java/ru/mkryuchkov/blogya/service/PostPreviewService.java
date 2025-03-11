package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;
import ru.mkryuchkov.blogya.util.PagingUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostPreviewService {

    private final PostPreviewRepository postPreviewRepository;

    private final PagingUtils pagingUtils;
    private final PostPreviewMapper postPreviewMapper;

    public List<PostPreviewDto> getPage(String tag, Integer page, Integer pageSize) {
        Integer offset = pagingUtils.getOffset(page, pageSize);
        List<PostPreview> posts;
        if (tag == null) {
            posts = postPreviewRepository.findAll(pageSize, offset);
        } else {
            posts = postPreviewRepository.findAllByTag(tag, pageSize, offset);
        }
        return Optional.ofNullable(posts).orElse(Collections.emptyList())
                .stream()
                .map(postPreviewMapper::toDto)
                .collect(Collectors.toList());
    }

}
