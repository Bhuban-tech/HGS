package org.example.hamrogharsewa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceCategoryRequestDto {

    @NotBlank(message = "Category name must not be empty")
    @Size(min = 3, max = 50, message = "Category name must be between 3 and 50 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @Size(max = 100, message = "Icon value must not exceed 100 characters")
    private String icon;
}
