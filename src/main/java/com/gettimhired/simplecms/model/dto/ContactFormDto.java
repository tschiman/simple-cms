package com.gettimhired.simplecms.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactFormDto(
        @NotBlank
        @Size(min = 1, max = 256)
        String name,
        @NotBlank
        @Size(min = 10, max = 64)
        String phoneNumber,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 1, max = 4096)
        String message
        ) {
}
