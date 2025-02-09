package ru.mkryuchkov.blogya.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mkryuchkov.blogya.model.PostPreview;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;
import ru.mkryuchkov.blogya.util.PagingUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostPreviewService {

    private final PostPreviewRepository postPreviewRepository;

    private final PagingUtils pagingUtils;

    public List<PostPreview> findAll(String tag, Integer page, Integer pageSize) {
        if (tag == null) {
            return postPreviewRepository.findAll(pageSize, pagingUtils.getOffset(page, pageSize));
        }
        return postPreviewRepository.findAllByTag(tag, pageSize, pagingUtils.getOffset(page, pageSize));
    }

}
