package com.gettimhired.dave.service;

import com.gettimhired.dave.model.dto.ContactDTO;
import com.gettimhired.dave.model.dto.ContactFormDto;
import com.gettimhired.dave.model.mongo.Contact;
import com.gettimhired.dave.model.mongo.ContactStatus;
import com.gettimhired.dave.repository.ContactRepository;
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
}
