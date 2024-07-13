package com.gettimhired.dave.model.dto;

import com.gettimhired.dave.model.mongo.Job;

public record JobDTO(
        String title,
        String mainImage,
        String description
) {
    public JobDTO(Job job) {
        this(
                job.title(),
                job.mainImage(),
                job.description()
        );
    }
}
