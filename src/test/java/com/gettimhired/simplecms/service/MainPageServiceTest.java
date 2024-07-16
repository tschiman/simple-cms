package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.MainPageDTO;
import com.gettimhired.simplecms.model.dto.MainPageEditDTO;
import com.gettimhired.simplecms.model.mongo.MainPage;
import com.gettimhired.simplecms.repository.MainPageRepository;
import com.gettimhired.simplecms.util.GzipUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MainPageServiceTest {

    @Mock
    private MainPageRepository mainPageRepository;

    @InjectMocks
    private MainPageService mainPageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInit() {
        when(mainPageRepository.findAll()).thenReturn(List.of());

        mainPageService.init();

        verify(mainPageRepository, times(1)).save(any(MainPage.class));
    }

    @Test
    public void testFindMainPage() {
        MainPage mainPage = new MainPage(UUID.randomUUID().toString(), "Title", new byte[0], "Section One", "one,two,three", "Section Two", "Content", "Section Three", "one,two,three", "Section 4 Title", "Section 4 content");
        when(mainPageRepository.findAll()).thenReturn(List.of(mainPage));

        Optional<MainPageDTO> mainPageDTO = mainPageService.findMainPage();

        assertTrue(mainPageDTO.isPresent());
        assertEquals(mainPage.id(), mainPageDTO.get().id());
        verify(mainPageRepository, times(1)).findAll();
    }

    @Test
    public void testUpdate() throws Exception {
        MainPage mainPage = new MainPage(UUID.randomUUID().toString(), "Title", new byte[0], "Section One", "one,two,three", "Section Two", "Content", "Section Three", "one,two,three", "Section 4 Title", "Section 4 content");
        MainPageEditDTO mainPageEditDTO = new MainPageEditDTO("Updated Title", false, new MockMultipartFile("mainPageImage", "mainPageImage.png", "image/png", "Updated Main image content".getBytes()), false, "Updated Section One", "Updated Section One Content", "Updated Section Two", "Updated Section Two Content", "Updated Section Three", "Updated Section Three Content", "Updated Section Four", "Updated Section Four Content");

        when(mainPageRepository.findAll()).thenReturn(List.of(mainPage));

        mainPageService.update(mainPageEditDTO);

        verify(mainPageRepository, times(1)).findAll();
        verify(mainPageRepository, times(1)).save(any(MainPage.class));
    }

    @Test
    public void testFindMainImage() throws Exception {
        MainPage mainPage = new MainPage(UUID.randomUUID().toString(), "Title", GzipUtil.compress("Main image content".getBytes()), "Section One", "one,two,three", "Section Two", "Content", "Section Three", "one,two,three", "Section 4 Title", "Section 4 content");

        when(mainPageRepository.findAll()).thenReturn(List.of(mainPage));

        byte[] mainImage = mainPageService.findMainImage();

        assertArrayEquals("Main image content".getBytes(), mainImage);
        verify(mainPageRepository, times(1)).findAll();
    }

    @Test
    public void testFindMainImage_NotFound() {
        when(mainPageRepository.findAll()).thenReturn(List.of());

        byte[] mainImage = mainPageService.findMainImage();

        assertArrayEquals(new byte[0], mainImage);
        verify(mainPageRepository, times(1)).findAll();
    }
}