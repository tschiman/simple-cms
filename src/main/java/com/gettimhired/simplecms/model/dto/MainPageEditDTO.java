package com.gettimhired.simplecms.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record MainPageEditDTO(
        @NotBlank
        @Size(min=1, max = 256, message = "Title must be between 1 and 256 characters")
        String title,
        Boolean hasMainPageImage,
        MultipartFile mainPageImage,
        Boolean deleteMainPageImage,
        @NotBlank
        @Size(min=1, max = 256, message = "Section 1 Title must be between 1 and 256 characters")
        String sectionOneTitle,
        @NotBlank
        @Size(min=1, max = 1000, message = "Section 1 Content must be between 1 and 1000 characters")
        String sectionOneContent,
        @NotBlank
        @Size(min=1, max = 256, message = "Section 2 Title must be between 1 and 256 characters")
        String sectionTwoTitle,
        @NotBlank
        @Size(min=1, max = 1000, message = "Section 2 Content must be between 1 and 1000 characters")
        String sectionTwoContent,
        @NotBlank
        @Size(min=1, max = 256, message = "Section 3 Title must be between 1 and 256 characters")
        String sectionThreeTitle,
        @NotBlank
        @Size(min=1, max = 1000, message = "Section 3 Content must be between 1 and 1000 characters")
        String sectionThreeContent
) {
}
