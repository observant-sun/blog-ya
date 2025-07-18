package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;
import ru.mkryuchkov.blogya.entity.FileEntity;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ImageControllerIntegrationTest extends BaseControllerIntegrationTest {

    @Test
    void findByUuid() throws Exception {
        FileEntity fileEntity = fileRepository.saveNewFile("content".getBytes());
        String id = fileEntity.id();
        mockMvc.perform(get("/image/" + id))
                .andExpect(status().isOk())
                .andExpect(content().bytes("content".getBytes()));

    }

    @Test
    void findByUuid_notFound() throws Exception {
        mockMvc.perform(get("/image/abcd"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

}
