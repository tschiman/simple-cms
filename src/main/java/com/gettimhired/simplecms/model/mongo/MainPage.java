package com.gettimhired.simplecms.model.mongo;

import org.springframework.data.annotation.Id;

public record MainPage(
        @Id
        String id,
        String title,
        byte[] mainImage,
        String sectionOneTitle,
        String sectionOneContent,
        String sectionTwoTitle,
        String sectionTwoContent,
        String sectionThreeTitle,
        String sectionThreeContent,
        String sectionFourTitle,
        String sectionFourContent
) {
}
