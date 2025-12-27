package org.example.hamrogharsewa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceCategoryRequestDto(

        @NotBlank(message = "Category name is required")
        @Size(min = 3, max = 50)
        String name,

        @Size(max = 500)
        String description,

        String icon
) {}
