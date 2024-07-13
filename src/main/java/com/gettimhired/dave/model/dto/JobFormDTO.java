package com.gettimhired.dave.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record JobFormDTO(
        @NotBlank
        @Size(min = 1, max = 256)
        String title,
        @NotBlank
        String mainImage,
        @NotBlank
        @Size(min = 1, max = 4096)
        String description
) {
}
