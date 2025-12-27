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
    private boolean active;

    // 🔥 THIS FIXES: UserResponseDto.from(user)
    public static UserResponseDto from(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profile(user.getProfile())
                .role(user.getRole() != null ? user.getRole().name() : null)
                .active(user.isActive())
                .build();
    }
}
