package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.MainPageDTO;
import com.gettimhired.simplecms.repository.MainPageRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MainPageService {

    private final MainPageRepository mainPageRepository;

    public MainPageService(MainPageRepository mainPageRepository) {
        this.mainPageRepository = mainPageRepository;
    }

    public Optional<MainPageDTO> getMainPage() {
        return mainPageRepository.findAll().stream().findFirst().map(mainPage -> new MainPageDTO(mainPage));
    }
}
