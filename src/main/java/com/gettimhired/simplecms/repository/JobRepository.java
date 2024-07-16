package com.gettimhired.simplecms.repository;

import com.gettimhired.simplecms.model.mongo.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface JobRepository extends MongoRepository<Job, String> {

    @Query(value = "{_id:  ?0}", fields = "{mainImage:  1}")
    Optional<Job> findJobByIdReturnMainImage(String id);

    @Query(value = "{_id:  ?0}", fields = "{subImage1:  1}")
    Job findSubImage1(String id);

    @Query(value = "{_id:  ?0}", fields = "{subImage2:  1}")
    Job findSubImage2(String id);

    @Query(value = "{_id:  ?0}", fields = "{subImage3:  1}")
    Job findSubImage3(String id);

    @Query(value = "{_id:  ?0}", fields = "{subImage4:  1}")
    Job findSubImage4(String id);
}
