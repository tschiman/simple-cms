package com.gettimhired.simplecms.model.dto;

import com.gettimhired.simplecms.model.mongo.Contact;

public record ContactDTO(
        String name,
        String phoneNumber,
        String email,
        String message
) {
    public ContactDTO(Contact contact) {
        this(
                contact.name(),
                contact.phoneNumber(),
                contact.email(),
                contact.message()
        );
    }
}
