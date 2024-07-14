package com.gettimhired.simplecms.model.dto;

import com.gettimhired.simplecms.model.mongo.Contact;
import com.gettimhired.simplecms.model.mongo.ContactStatus;

public record ContactDTO(
        String id,
        String name,
        String phoneNumber,
        String email,
        String message,
        ContactStatus contactStatus
) {
    public ContactDTO(Contact contact) {
        this(
                contact.id(),
                contact.name(),
                contact.phoneNumber(),
                contact.email(),
                contact.message(),
                contact.contactStatus()
        );
    }
}
