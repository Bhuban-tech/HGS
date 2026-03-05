package org.example.hamrogharsewa.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String userName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-{}\\[\\]().,<>/?]).+$", message = "Password must include uppercase, lowercase, number, and special character")
    private String password;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    private String role;

    private String address;
    private String category;
    private String serviceCategoryId; // frontend sends serviceCategoryId
    private String experience;

    // Returns whichever category field was provided
    @JsonIgnore
    public String getResolvedCategoryId() {
        if (serviceCategoryId != null && !serviceCategoryId.isBlank()) return serviceCategoryId;
        return category;
    }

}
