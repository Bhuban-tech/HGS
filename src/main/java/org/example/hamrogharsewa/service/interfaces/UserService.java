package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.BecomeProviderDto;
import org.example.hamrogharsewa.dto.response.UpdateProfileDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserResponseDto updateUserProfile(UpdateProfileDto updatedInfo);

    UserResponseDto becomeProvider(BecomeProviderDto dto); // ✅ Added

    void changePassword(String oldPassword, String newPassword);

    void forgotPassword(String email);

    boolean verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    List<UserResponseDto> getProvidersByCategory(String categoryId);

    List<UserResponseDto> getAllProviders(); // ✅ Added

    UserResponseDto getProviderById(String id); // ✅ Added

    List<UserResponseDto> getAllUsers();

    void requestEmailChange(String newEmail);

    void confirmEmailChange(String newEmail, String otp);
}