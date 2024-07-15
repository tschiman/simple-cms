package com.gettimhired.simplecms.model.dto;

import org.springframework.web.multipart.MultipartFile;

public record MainPageEditDTO(
        String title,
        Boolean hasMainPageImage,
        MultipartFile mainPageImage,
        String sectionOneTitle,
        String sectionOneContent,
        String sectionTwoTitle,
        String sectionTwoContent,
        String sectionThreeTitle,
        String sectionThreeContent
) {
}
