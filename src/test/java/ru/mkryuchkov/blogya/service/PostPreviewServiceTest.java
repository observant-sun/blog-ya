package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mkryuchkov.blogya.ServiceTestConfig;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.reset;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
class PostPreviewServiceTest {

    @Autowired
    private PostPreviewService postPreviewService;

    @Autowired
    private PostPreviewRepository postPreviewRepository;

    @BeforeEach
    void resetMocks() {
        reset(postPreviewRepository);
    }

    @Test
    void getPage() {
        Timestamp timestamp = new Timestamp(414141441L);
        Long offset = 6L;
        int page = 3;
        int pageSize = 2;
        PostPreview postPreview1 = new PostPreview(1L, "title1", "bodyPreview1", "imageUuid", "tags", 123, 1234, timestamp, timestamp);
        PostPreview postPreview2 = new PostPreview(2L, "title1", "bodyPreview1", "imageUuid", "tags", 123, 1234, timestamp, timestamp);
        String tag = "tag";
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);
        doReturn(List.of(postPreview1, postPreview2)).when(postPreviewRepository).findAllByTag(tag, pageSize, offset);
        List<PostPreviewDto> actual = postPreviewService.getPage(tag, pageable);
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }
}