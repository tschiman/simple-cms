package com.gettimhired.simplecms.service;

import com.gettimhired.simplecms.model.dto.ContactDTO;
import com.gettimhired.simplecms.model.dto.ContactFormDto;
import com.gettimhired.simplecms.model.mongo.Contact;
import com.gettimhired.simplecms.model.mongo.ContactStatus;
import com.gettimhired.simplecms.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContactService {
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void save(ContactFormDto contactFormDto) {
        var contact = new Contact(
                UUID.randomUUID().toString(),
                contactFormDto.name(),
                contactFormDto.phoneNumber(),
                contactFormDto.email(),
                contactFormDto.message(),
                ContactStatus.NEW
        );
        contactRepository.save(contact);
    }

    public List<ContactDTO> findAllContacts() {
        return contactRepository.findAll().stream()
                .map(ContactDTO::new)
                .toList();

    }

    public void updateContactStatus(String id, ContactStatus status) {
        var contactOpt = contactRepository.findById(id);
        if (contactOpt.isPresent()) {
            var contactToSave = new Contact(
                    contactOpt.get().id(),
                    contactOpt.get().name(),
                    contactOpt.get().phoneNumber(),
                    contactOpt.get().email(),
                    contactOpt.get().message(),
                    status
            );
            contactRepository.save(contactToSave);
        }
    }
}
