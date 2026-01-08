package org.example.hamrogharsewa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ServiceRequestCreateDto(

        @NotBlank
        String serviceProviderId,

        @NotBlank
        String serviceCategoryId,

        @NotBlank
        @Size(min = 10, max = 500)
        String description,

        @NotBlank
        @Size(min = 5, max = 200)
        String address
)
{}

