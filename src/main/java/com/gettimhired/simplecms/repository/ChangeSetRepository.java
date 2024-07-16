package com.gettimhired.simplecms.repository;

import com.gettimhired.simplecms.model.mongo.ChangeSet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeSetRepository extends MongoRepository<ChangeSet, String> {
}
