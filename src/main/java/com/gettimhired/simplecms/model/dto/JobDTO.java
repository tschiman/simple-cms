package com.gettimhired.simplecms.model.dto;

import com.gettimhired.simplecms.model.mongo.Job;

public record JobDTO(
        String id,
        String title,
        boolean hasMainImage,
        boolean hasSubImage1,
        boolean hasSubImage2,
        boolean hasSubImage3,
        boolean hasSubImage4,
        String description
) {
    public JobDTO(Job job) {
        this(
                job.id(),
                job.title(),
                job.mainImage().length > 0,
                job.subImage1().length > 0,
                job.subImage2().length > 0,
                job.subImage3().length > 0,
                job.subImage4().length > 0,
                job.description()
        );
    }
}
