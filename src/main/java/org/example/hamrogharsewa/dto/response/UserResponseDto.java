package org.example.hamrogharsewa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hamrogharsewa.model.User;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String profile;
    private String role;
    private boolean active;
    private String serviceCategoryId;

    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getProfile(),
                user.getRole() != null ? user.getRole().name() : null,
                user.isActive(),
                user.getServiceCategoryId()
        );
    }
        // ✅ add this
}