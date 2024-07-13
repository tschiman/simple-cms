package com.gettimhired.dave.model.dto;

import com.gettimhired.dave.model.mongo.Job;

public record JobDTO(
        String id,
        String title,
        String mainImageId,
        String description
) {
    public JobDTO(Job job) {
        this(
                job.id(),
                job.title(),
                job.mainImageId(),
                job.description()
        );
    }
}
