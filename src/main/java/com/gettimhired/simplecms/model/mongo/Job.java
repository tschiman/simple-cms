package com.gettimhired.simplecms.model.mongo;

import org.springframework.data.annotation.Id;

public record Job(
        @Id
        String id,
        String title,
        byte[] mainImage,
        byte[] subImage1,
        byte[] subImage2,
        byte[] subImage3,
        byte[] subImage4,
        String description,
        Long createDate
) {
}
