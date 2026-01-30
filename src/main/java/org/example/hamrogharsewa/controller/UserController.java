package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.UserResponseDto;
import org.example.hamrogharsewa.service.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // --- PROFILE ENDPOINTS ---

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponseDto.success("Profile fetched", userService.getCurrentUser()));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(
            @Valid @RequestBody UserRegistrationDto dto) {
        return ResponseEntity.ok(ApiResponseDto.success("Profile updated", userService.updateUserProfile(dto)));
    }

    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("Password changed successfully", null));
    }

    // --- BROWSE PROVIDERS ---

    @GetMapping("/providers/category/{categoryId}")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getProvidersByCategory(
            @PathVariable String categoryId) {
        return ResponseEntity
                .ok(ApiResponseDto.success("Providers fetched", userService.getProvidersByCategory(categoryId)));
    }

    // --- FORGOT PASSWORD (PUBLIC) ---

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponseDto<Void>> forgotPassword(@RequestParam String email) {
        userService.forgotPassword(email);
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent to email", null));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDto<Void>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {
        userService.verifyOtp(email, otp);
        return ResponseEntity.ok(ApiResponseDto.success("OTP verified", null));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<Void>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("Password reset successfully", null));
    }
}
