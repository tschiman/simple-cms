package com.gettimhired.dave.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record JobEditDTO(

        @NotBlank
        @Size(min = 1, max = 256)
        String id,
        String title,
        Boolean hasMainImage,
        Boolean hasSubImage1,
        MultipartFile subImage1,
        Boolean deleteSubImage1,
        Boolean hasSubImage2,
        MultipartFile subImage2,
        Boolean deleteSubImage2,
        Boolean hasSubImage3,
        Boolean deleteSubImage3,
        MultipartFile subImage3,
        Boolean hasSubImage4,
        Boolean deleteSubImage4,
        MultipartFile subImage4,
        @NotBlank
        @Size(min = 1, max = 4096)
        String description) {
}
