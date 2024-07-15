package com.gettimhired.simplecms.model.mongo;

import org.springframework.data.annotation.Id;

import java.util.List;

public record MainPage(
        @Id
        String id,
        String title,
        byte[] mainImage,
        String sectionOneTitle,
        List<String> sectionOneContent,
        String sectionTwoTitle,
        String sectionTwoContent,
        String sectionThreeTitle,
        List<String> sectionThreeContent
) {
}
