package com.gettimhired.simplecms.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record JobFormDTO(
        @NotBlank
        @Size(min = 1, max = 256)
        String title,
        @NotNull
        MultipartFile mainImage,
        MultipartFile subImage1,
        MultipartFile subImage2,
        MultipartFile subImage3,
        MultipartFile subImage4,
        @NotBlank
        @Size(min = 1, max = 4096)
        String description
) {
}
