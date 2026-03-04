package org.example.hamrogharsewa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hamrogharsewa.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private String id;
    private String userName;
    private String email;
    private String phoneNumber;
    private String profile;
    private String role;
    private String address;
    private String serviceCategoryId;
    private String name; // Alias for userName
    private String location; // Alias for address
    private String service; // Alias for serviceCategoryId
    private String status; // "Available" or "Unavailable"
    private String rate; // Placeholder rate
    private boolean active;
    private boolean approved;

    // 🔥 THIS FIXES: UserResponseDto.from(user)
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .name(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profile(user.getProfile())
                .address(user.getAddress())
                .location(user.getAddress())
                .serviceCategoryId(user.getServiceCategoryId())
                .service(user.getServiceCategoryId()) // Mapping to ID for now
                .status(user.isActive() ? "Available" : "Unavailable")
                .rate("Rs. 500/hr") // Default placeholder
                .role(user.getRole() != null ? user.getRole().name() : null)
                .active(user.isActive())
                .approved(user.isApproved())
                .build();
    }
}
