package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.UpdateProfileDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserResponseDto updateUserProfile(UpdateProfileDto updatedInfo);

    void changePassword(String oldPassword, String newPassword);

    void forgotPassword(String email);

    boolean verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    java.util.List<UserResponseDto> getProvidersByCategory(String categoryId);

    List<UserResponseDto> getAllUsers();

    void requestEmailChange(String newEmail);
    void confirmEmailChange(String newEmail, String otp);
}
