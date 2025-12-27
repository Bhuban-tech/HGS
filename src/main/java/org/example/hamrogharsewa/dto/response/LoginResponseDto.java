package org.example.hamrogharsewa.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.hamrogharsewa.model.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private String token;
    private String id;
    private String userName;
    private String email;
    private Role role;
}
