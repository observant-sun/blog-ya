package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.mkryuchkov.blogya.dto.PostCommentDto;
import ru.mkryuchkov.blogya.dto.PostDto;
import ru.mkryuchkov.blogya.dto.PostPreviewDto;
import ru.mkryuchkov.blogya.entity.FileEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PostPreviewControllerTest extends BaseControllerTest {

    @Test
    void findAll_nullPage_nullPageSize() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostPreviewDto postPreviewDto1 = new PostPreviewDto(1L, "text1", "body1", "imageUuid1", "tags1", 1, 2, timestamp, timestamp);
        PostPreviewDto postPreviewDto2 = new PostPreviewDto(2L, "text2", "body2", "imageUuid2", "tags1", 3, 4, timestamp, timestamp);
        List<PostPreviewDto> postPreviewDtos = List.of(postPreviewDto1, postPreviewDto2);
        String tag = "tags1";
        PageRequest paging = PageRequest.of(0, 10);

        when(postPreviewService.getPage(tag, paging)).thenReturn(postPreviewDtos);

        mockMvc.perform(get("/posts")
                        .param("tag", tag))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts", postPreviewDtos))
                .andExpect(model().attribute("paging", paging));

        verify(postPreviewService).getPage(tag, paging);
    }

    @Test
    void findAll_nullTag() throws Exception {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        PostPreviewDto postPreviewDto1 = new PostPreviewDto(1L, "text1", "body1", "imageUuid1", "tags1", 1, 2, timestamp, timestamp);
        PostPreviewDto postPreviewDto2 = new PostPreviewDto(2L, "text2", "body2", "imageUuid2", "tags2", 3, 4, timestamp, timestamp);
        List<PostPreviewDto> postPreviewDtos = List.of(postPreviewDto1, postPreviewDto2);
        PageRequest paging = PageRequest.of(3, 2);

        when(postPreviewService.getPage(null, paging)).thenReturn(postPreviewDtos);

        mockMvc.perform(get("/posts")
                        .param("page", "3")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts"))
                .andExpect(model().attribute("posts", postPreviewDtos))
                .andExpect(model().attribute("paging", paging));

        verify(postPreviewService).getPage(null, paging);
    }
}
