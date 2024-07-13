package com.gettimhired.dave.model.mongo;

import org.springframework.data.annotation.Id;

public record Job(
        @Id
        String id,
        String title,
        String mainImageId,
        byte[] mainImage,
        String description
) {
}
