package ru.mkryuchkov.blogya.controller;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class ImageControllerTest extends BaseControllerTest {

    @Test
    void findByUuid() throws Exception {
        byte[] bytes = {1, 2, 3};
        when(fileService.findById("abcd")).thenReturn(Optional.of(bytes));

        mockMvc.perform(get("/image/abcd"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(bytes));

        verify(fileService).findById("abcd");
    }

    @Test
    void findByUuid_notFound() throws Exception {
        when(fileService.findById("abcd")).thenReturn(Optional.empty());

        mockMvc.perform(get("/image/abcd"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(fileService).findById("abcd");
    }

}
