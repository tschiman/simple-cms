package com.gettimhired.simplecms.model.dto;

import com.gettimhired.simplecms.model.mongo.MainPage;

import java.util.List;

public record MainPageDTO(
        String id,
        String title,
        Boolean hasMainPageImage,
        String sectionOneTitle,
        List<String> sectionOneContent,
        String sectionTwoTitle,
        String sectionTwoContent,
        String sectionThreeTitle,
        List<String> sectionThreeContent
) {
    public MainPageDTO(MainPage mainPage) {
        this(
                mainPage.id(),
                mainPage.title(),
                mainPage.mainImage().length > 0,
                mainPage.sectionOneTitle(),
                mainPage.sectionOneContent(),
                mainPage.sectionTwoTitle(),
                mainPage.sectionTwoContent(),
                mainPage.sectionThreeTitle(),
                mainPage.sectionThreeContent()
        );
    }
}
