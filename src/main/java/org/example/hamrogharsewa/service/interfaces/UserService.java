package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserResponseDto updateUserProfile(UserRegistrationDto updatedInfo);

    void changePassword(String oldPassword, String newPassword);

    void forgotPassword(String email);

    boolean verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);
}
