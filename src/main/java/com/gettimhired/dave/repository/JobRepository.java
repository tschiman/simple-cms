package com.gettimhired.dave.repository;

import com.gettimhired.dave.model.mongo.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, String> {
    Optional<Job> findByIdAndMainImageId(String id, String imageId);
}
