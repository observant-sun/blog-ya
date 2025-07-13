package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.mkryuchkov.blogya.ServiceTestConfig;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;
import ru.mkryuchkov.blogya.util.PagingUtils;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ServiceTestConfig.class)
class PostPreviewServiceTest {

    @Autowired
    private PostPreviewService postPreviewService;

    @Autowired
    private PostPreviewRepository postPreviewRepository;
    @Autowired
    private PostPreviewMapper postPreviewMapper;
    @Autowired
    private PagingUtils pagingUtils;

    @Test
    void getPage() {
        Timestamp timestamp = new Timestamp(414141441L);
        Integer offset = 6;
        int page = 3;
        int pageSize = 2;
        PostPreview postPreview1 = new PostPreview(1L, "title1", "bodyPreview1", "imageUuid", "tags", 123, 1234, timestamp, timestamp);
        PostPreview postPreview2 = new PostPreview(2L, "title1", "bodyPreview1", "imageUuid", "tags", 123, 1234, timestamp, timestamp);
        doReturn(offset).when(pagingUtils).getOffset(page, pageSize);
        String tag = "tag";
        doReturn(List.of(postPreview1, postPreview2)).when(postPreviewRepository).findAllByTag(tag, pageSize, offset);
        List<PostPreviewDto> actual = postPreviewService.getPage(tag, page, pageSize);
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }
}