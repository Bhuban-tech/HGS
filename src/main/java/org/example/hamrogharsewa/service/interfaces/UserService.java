package org.example.hamrogharsewa.service.interfaces;

import org.example.hamrogharsewa.dto.request.BecomeProviderDto;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto getCurrentUser();

    UserResponseDto updateUserProfile(UserRegistrationDto updatedInfo);

    void changePassword(String oldPassword, String newPassword);

    void forgotPassword(String email);

    boolean verifyOtp(String email, String otp);

    void resetPassword(String email, String newPassword);

    java.util.List<UserResponseDto> getProvidersByCategory(String categoryId);

    java.util.List<UserResponseDto> getAllProviders();

    UserResponseDto becomeProvider(BecomeProviderDto dto);

    UserResponseDto getProviderById(String id);
}
