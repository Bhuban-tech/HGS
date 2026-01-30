package org.example.hamrogharsewa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRequestCreateDto {

        @NotBlank
        private String serviceProviderId;

        @NotBlank
        private String serviceCategoryId;

        @NotBlank
        @Size(min = 10, max = 500)
        private String description;

        @NotBlank
        @Size(min = 5, max = 200)
        private String address;
}
