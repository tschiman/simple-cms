package com.gettimhired.simplecms.repository;

import com.gettimhired.simplecms.model.mongo.Job;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobRepository extends MongoRepository<Job, String> {

}
