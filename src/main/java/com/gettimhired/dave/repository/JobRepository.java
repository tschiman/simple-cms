package com.gettimhired.dave.repository;

import com.gettimhired.dave.model.mongo.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {

}
