package com.gettimhired.dave.model.dto;

import com.gettimhired.dave.model.mongo.Contact;

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
