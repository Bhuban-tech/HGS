package org.example.hamrogharsewa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.hamrogharsewa.dto.request.BecomeProviderDto;
import org.example.hamrogharsewa.dto.request.UserRegistrationDto;
import org.example.hamrogharsewa.dto.response.ApiResponseDto;
import org.example.hamrogharsewa.dto.response.UpdateProfileDto;
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

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponseDto.success("Profile fetched", userService.getCurrentUser()));
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(
            @RequestBody UpdateProfileDto dto) {
        return ResponseEntity.ok(ApiResponseDto.success("Profile updated", userService.updateUserProfile(dto)));
    }

    @PatchMapping("/become-provider")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> becomeProvider(
            @Valid @RequestBody BecomeProviderDto dto) {
        return ResponseEntity
                .ok(ApiResponseDto.success("Provider application submitted", userService.becomeProvider(dto)));
    }

    @PatchMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword) {
        userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("Password changed successfully", null));
    }

    @GetMapping("/providers/category/{categoryId}")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getProvidersByCategory(
            @PathVariable String categoryId) {
        return ResponseEntity
                .ok(ApiResponseDto.success("Providers fetched", userService.getProvidersByCategory(categoryId)));
    }

    @GetMapping("/providers")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllProviders() {
        return ResponseEntity
                .ok(ApiResponseDto.success("Providers fetched", userService.getAllProviders()));
    }

    @GetMapping("/providers/{id}")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getProviderById(
            @PathVariable String id) {
        return ResponseEntity
                .ok(ApiResponseDto.success("Provider fetched", userService.getProviderById(id)));
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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponseDto<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponseDto.success("Users fetched", userService.getAllUsers()));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<Void>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("Password reset successfully", null));
    }
    @PostMapping("/request-email-change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> requestEmailChange(@RequestParam String newEmail) {
        userService.requestEmailChange(newEmail);
        return ResponseEntity.ok(ApiResponseDto.success("OTP sent to new email", null));
    }

    @PostMapping("/confirm-email-change")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> confirmEmailChange(
            @RequestParam String newEmail,
            @RequestParam String otp) {
        userService.confirmEmailChange(newEmail, otp);
        return ResponseEntity.ok(ApiResponseDto.success("Email updated successfully", null));
    }
}
