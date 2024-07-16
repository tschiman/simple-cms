package com.gettimhired.simplecms.config;

import com.gettimhired.simplecms.model.mongo.ChangeSet;
import com.gettimhired.simplecms.model.mongo.Contact;
import com.gettimhired.simplecms.model.mongo.Job;
import com.gettimhired.simplecms.repository.ChangeSetRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class MongoSchemaManager {
    Logger log = LoggerFactory.getLogger(MongoSchemaManager.class);
    private final MongoTemplate mongoTemplate;
    private final ChangeSetRepository changeSetRepository;

    public MongoSchemaManager(MongoTemplate mongoTemplate, ChangeSetRepository changeSetRepository) {
        this.mongoTemplate = mongoTemplate;
        this.changeSetRepository = changeSetRepository;
    }

    @PostConstruct
    public void init() {
        doChangeSet(
                "changeset-001",
                "tim.schimandle",
                "add createDate to contacts and jobs",
                () -> {
                    List<Contact> contacts = mongoTemplate.findAll(Contact.class);
                    contacts.stream()
                            .filter(c -> c.createDate() == null)
                            .map(c -> new Contact(c.id(),c.name(),c.phoneNumber(),c.email(),c.message(),c.contactStatus(), Instant.now().getEpochSecond()))
                            .forEach(mongoTemplate::save);

                    List<Job> jobs = mongoTemplate.findAll(Job.class);
                    jobs.stream()
                            .filter(j -> j.createDate() == null)
                            .map(j -> new Job(j.id(),j.title(),j.mainImage(),j.subImage1(),j.subImage2(),j.subImage3(),j.subImage4(), j.description(), Instant.now().getEpochSecond()))
                            .forEach(mongoTemplate::save);
                });

    }

    private void doChangeSet(String id, String author, String description, Runnable change) {
        log.debug("Starting change set |id: {} |author: {} |description: {}", id, author, description);
        var changeSet = new ChangeSet();
        changeSet.setId(id);
        changeSet.setAuthor(author);
        changeSet.setDescription(description);
        changeSet.setCreatedDate(System.currentTimeMillis());

        var changeSetFromDb = changeSetRepository.findById(id);

        if (
                changeSetFromDb.isEmpty() || //change set doesn't exist
                (!changeSetFromDb.get().isInProgress() && !changeSetFromDb.get().isCompleted()) //change is not complete and not in progress
        ) {
            try {
                changeSetRepository.save(changeSet); //starts work
                //do the work
                log.info("Running change set |id: {} |author: {} |description: {}", id, author, description);
                change.run();

                //update the changeset
                var changeSet1 = changeSetRepository.findById(changeSet.getId());
                changeSet1.ifPresent(c -> {
                    c.setInProgress(false);
                    c.setCompleted(true);
                    changeSetRepository.save(c);
                });
            } catch (Exception e) {
                log.debug("Error with change set |id: {} |author: {} |description: {}", id, author, description);
                //fail the changeset if there's an issue
                changeSet.setInProgress(false);
                changeSet.setCompleted(false);
                changeSet.setDescription(e.getMessage());
                changeSetRepository.save(changeSet);
                //throw the exception again to stop initialization
                throw e;
            }
        } else {
            //skip, already applied
        }
        log.debug("Completed change set |id: {} |author: {} |description: {}", id, author, description);
    }
}
