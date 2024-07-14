package com.gettimhired.simplecms.repository;

import com.gettimhired.simplecms.model.mongo.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
