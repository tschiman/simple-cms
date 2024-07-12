package com.gettimhired.dave.repository;

import com.gettimhired.dave.model.mongo.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
