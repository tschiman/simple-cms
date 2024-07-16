package com.gettimhired.simplecms.controller;

import com.gettimhired.simplecms.service.JobService;
import com.gettimhired.simplecms.service.MainPageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ImageControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private MainPageService mainPageService;

    @InjectMocks
    private ImageController imageController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void testGetMainImageById() throws Exception {
        byte[] imageBytes = "test image".getBytes();
        when(jobService.findJobMainImage("1")).thenReturn(imageBytes);

        mockMvc.perform(get("/job/1/main-image"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/jpeg"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length)))
                .andExpect(content().bytes(imageBytes));

        verify(jobService, times(1)).findJobMainImage("1");
    }

    @Test
    public void testGetMainImageById_NotFound() throws Exception {
        when(jobService.findJobMainImage("1")).thenReturn(new byte[0]);

        mockMvc.perform(get("/job/1/main-image"))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).findJobMainImage("1");
    }

    @Test
    public void testGetSubImageById() throws Exception {
        byte[] imageBytes = "test sub image".getBytes();
        when(jobService.findJobSubImage("1", "2")).thenReturn(imageBytes);

        mockMvc.perform(get("/job/1/sub-image/2"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/jpeg"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length)))
                .andExpect(content().bytes(imageBytes));

        verify(jobService, times(1)).findJobSubImage("1", "2");
    }

    @Test
    public void testGetSubImageById_NotFound() throws Exception {
        when(jobService.findJobSubImage("1", "2")).thenReturn(new byte[0]);

        mockMvc.perform(get("/job/1/sub-image/2"))
                .andExpect(status().isNotFound());

        verify(jobService, times(1)).findJobSubImage("1", "2");
    }

    @Test
    public void testGetMainImage() throws Exception {
        byte[] imageBytes = "main image".getBytes();
        when(mainPageService.findMainImage()).thenReturn(imageBytes);

        mockMvc.perform(get("/main-image"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "image/jpeg"))
                .andExpect(header().string(HttpHeaders.CONTENT_LENGTH, String.valueOf(imageBytes.length)))
                .andExpect(content().bytes(imageBytes));

        verify(mainPageService, times(1)).findMainImage();
    }

    @Test
    public void testGetMainImage_NotFound() throws Exception {
        when(mainPageService.findMainImage()).thenReturn(new byte[0]);

        mockMvc.perform(get("/main-image"))
                .andExpect(status().isNotFound());

        verify(mainPageService, times(1)).findMainImage();
    }
}