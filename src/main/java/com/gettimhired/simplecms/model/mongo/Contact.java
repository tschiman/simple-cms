package com.gettimhired.simplecms.model.mongo;

import org.springframework.data.annotation.Id;

public record Contact(
        @Id
        String id,
        String name,
        String phoneNumber,
        String email,
        String message,
        ContactStatus contactStatus,
        Long createDate
) {
}
