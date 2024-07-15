package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.MainPageDTO;
import com.gettimhired.simplecms.model.dto.MainPageEditDTO;
import com.gettimhired.simplecms.model.mongo.MainPage;
import com.gettimhired.simplecms.repository.MainPageRepository;
import com.gettimhired.simplecms.util.GzipUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class MainPageService {

    private final MainPageRepository mainPageRepository;

    public MainPageService(MainPageRepository mainPageRepository) {
        this.mainPageRepository = mainPageRepository;
    }

    @PostConstruct
    public void init() {
        var mainPages = mainPageRepository.findAll();
        if (mainPages.size() == 0) {
            var mainPage = new MainPage(
                    UUID.randomUUID().toString(),
                    "Temporary Title",
                    new byte[0],
                    "Section One",
                    "one,two,three",
                    "Section Two",
                    "This is some paragraph content",
                    "Section Three",
                    "one,two,three"
            );
            mainPageRepository.save(mainPage);
        }
    }

    public Optional<MainPageDTO> findMainPage() {
        return mainPageRepository.findAll().stream().findFirst()
                .map(MainPageDTO::new);
    }

    public void update(MainPageEditDTO mainPageEditDto) {
        var mainPageOpt = mainPageRepository.findAll().stream().findFirst();
        try {
            var mainPageToSave = new MainPage(
                    mainPageOpt.isPresent() ? mainPageOpt.get().id() : UUID.randomUUID().toString(),
                    mainPageEditDto.title(),
                    GzipUtil.compress(mainPageEditDto.mainPageImage().getBytes()),
                    mainPageEditDto.sectionOneTitle(),
                    mainPageEditDto.sectionOneContent(),
                    mainPageEditDto.sectionTwoTitle(),
                    mainPageEditDto.sectionTwoContent(),
                    mainPageEditDto.sectionThreeTitle(),
                    mainPageEditDto.sectionThreeContent()
                    );
            mainPageRepository.save(mainPageToSave);
        } catch (Exception e) {
            //TODO
        }
    }

    public byte[] findMainImage() {
        try {
            var mainPageOpt = mainPageRepository.findAll().stream().findFirst();
            if (mainPageOpt.isPresent()) {
                return GzipUtil.decompress(mainPageOpt.get().mainImage());
            }
        } catch (Exception e) {
            //TODO
            return new byte[0];
        }
        return new byte[0];
    }
}
