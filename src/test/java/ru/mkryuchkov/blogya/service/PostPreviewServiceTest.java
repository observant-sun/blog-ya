package ru.mkryuchkov.blogya.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.PostPreview;
import ru.mkryuchkov.blogya.mapper.PostPreviewMapper;
import ru.mkryuchkov.blogya.repository.PostPreviewRepository;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = PostPreviewService.class)
class PostPreviewServiceTest {

    @Autowired
    private PostPreviewService postPreviewService;

    @MockitoBean
    private PostPreviewRepository postPreviewRepository;
    @MockitoBean
    private PostPreviewMapper postPreviewMapper;

    private void verifyNoMoreMockInteractions() {
        verifyNoMoreInteractions(postPreviewRepository, postPreviewMapper);
    }

    @Test
    void getPage_nullTag() {
        Timestamp timestamp = new Timestamp(414141441L);
        Long offset = 6L;
        int page = 3;
        int pageSize = 2;
        PostPreview postPreview1 = new PostPreview(1L, "title1", "bodyPreview1", "imageUuid1", "tags1", 123, 1234, timestamp, timestamp);
        PostPreviewDto postPreviewDto1 = new PostPreviewDto(1L, "title1", "bodyPreview1", "imageUuid1", "tags", 123, 1234, timestamp, timestamp);
        PostPreview postPreview2 = new PostPreview(2L, "title2", "bodyPreview2", "imageUuid2", "tags2", 1236, 12345, timestamp, timestamp);
        PostPreviewDto postPreviewDto2 = new PostPreviewDto(2L, "title2", "bodyPreview2", "imageUuid2", "tags2", 1236, 12345, timestamp, timestamp);
        String tag = null;
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);

        when(postPreviewRepository.findAll(pageSize, offset)).thenReturn(List.of(postPreview1, postPreview2));
        when(postPreviewMapper.toDto(postPreview1)).thenReturn(postPreviewDto1);
        when(postPreviewMapper.toDto(postPreview2)).thenReturn(postPreviewDto2);

        List<PostPreviewDto> actual = postPreviewService.getPage(tag, pageable);

        verify(postPreviewRepository).findAll(pageSize, offset);
        verify(postPreviewMapper).toDto(postPreview1);
        verify(postPreviewMapper).toDto(postPreview2);
        verifyNoMoreMockInteractions();

        assertEquals(List.of(postPreviewDto1, postPreviewDto2), actual);
    }

    @Test
    void getPage_blankTag() {
        Timestamp timestamp = new Timestamp(414141441L);
        Long offset = 6L;
        int page = 3;
        int pageSize = 2;
        PostPreview postPreview1 = new PostPreview(1L, "title1", "bodyPreview1", "imageUuid1", "tags1", 123, 1234, timestamp, timestamp);
        PostPreviewDto postPreviewDto1 = new PostPreviewDto(1L, "title1", "bodyPreview1", "imageUuid1", "tags", 123, 1234, timestamp, timestamp);
        PostPreview postPreview2 = new PostPreview(2L, "title2", "bodyPreview2", "imageUuid2", "tags2", 1236, 12345, timestamp, timestamp);
        PostPreviewDto postPreviewDto2 = new PostPreviewDto(2L, "title2", "bodyPreview2", "imageUuid2", "tags2", 1236, 12345, timestamp, timestamp);
        String tag = " ";
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);

        when(postPreviewRepository.findAll(pageSize, offset)).thenReturn(List.of(postPreview1, postPreview2));
        when(postPreviewMapper.toDto(postPreview1)).thenReturn(postPreviewDto1);
        when(postPreviewMapper.toDto(postPreview2)).thenReturn(postPreviewDto2);

        List<PostPreviewDto> actual = postPreviewService.getPage(tag, pageable);

        verify(postPreviewRepository).findAll(pageSize, offset);
        verify(postPreviewMapper).toDto(postPreview1);
        verify(postPreviewMapper).toDto(postPreview2);

        assertEquals(List.of(postPreviewDto1, postPreviewDto2), actual);
    }

    @Test
    void getPage_nonEmptyTag() {
        Timestamp timestamp = new Timestamp(414141441L);
        Long offset = 6L;
        int page = 3;
        int pageSize = 2;
        PostPreview postPreview1 = new PostPreview(1L, "title1", "bodyPreview1", "imageUuid1", "tags", 123, 1234, timestamp, timestamp);
        PostPreviewDto postPreviewDto1 = new PostPreviewDto(1L, "title1", "bodyPreview1", "imageUuid1", "tags", 123, 1234, timestamp, timestamp);
        PostPreview postPreview2 = new PostPreview(2L, "title2", "bodyPreview2", "imageUuid2", "tags", 1236, 12345, timestamp, timestamp);
        PostPreviewDto postPreviewDto2 = new PostPreviewDto(2L, "title2", "bodyPreview2", "imageUuid2", "tags", 1236, 12345, timestamp, timestamp);
        String tag = "tag";
        Pageable pageable = Pageable.ofSize(pageSize).withPage(page);

        when(postPreviewRepository.findAllByTag(tag, pageSize, offset)).thenReturn(List.of(postPreview1, postPreview2));
        when(postPreviewMapper.toDto(postPreview1)).thenReturn(postPreviewDto1);
        when(postPreviewMapper.toDto(postPreview2)).thenReturn(postPreviewDto2);

        List<PostPreviewDto> actual = postPreviewService.getPage(tag, pageable);

        verify(postPreviewRepository).findAllByTag(tag, pageSize, offset);
        verify(postPreviewMapper).toDto(postPreview1);
        verify(postPreviewMapper).toDto(postPreview2);

        assertEquals(List.of(postPreviewDto1, postPreviewDto2), actual);
    }
}