package com.gettimhired.simplecms.repository;

import com.gettimhired.simplecms.model.mongo.MainPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MainPageRepository extends MongoRepository<MainPage, String> {
}
