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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/service-requests")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> getCurrentUser() {
        UserResponseDto user = userService.getCurrentUser();
        return ResponseEntity.ok(
                ApiResponseDto.success("Booking Created Successfully", user)
        );
    }

    @PutMapping("/service-requests/{id}/cancel ")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<UserResponseDto>> updateProfile(
            @Valid @RequestBody UserRegistrationDto dto
    ) {
        UserResponseDto updatedUser = userService.updateUserProfile(dto);
        return ResponseEntity.ok(
                ApiResponseDto.success("cancle ", updatedUser)
        );
    }

    @PatchMapping("/service-requests/{id}/complete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<Void>> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        userService.changePassword(oldPassword, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("mark as a complete"));
    }


    @PostMapping("/service-requests/my-requests")
    public ResponseEntity<ApiResponseDto<Void>> forgotPassword(
            @RequestParam String email
    ) {
        userService.forgotPassword(email);
        return ResponseEntity.ok(ApiResponseDto.success("view  my booking "));
    }

    @PostMapping("/chat/send ")
    public ResponseEntity<ApiResponseDto<Void>> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        userService.verifyOtp(email, otp);
        return ResponseEntity.ok(ApiResponseDto.success("message send  successfully"));
    }

    @PostMapping("/chat/{requestId}")
    public ResponseEntity<ApiResponseDto<Void>> resetPassword(
            @RequestParam String email,
            @RequestParam String newPassword
    ) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok(ApiResponseDto.success("get message successfully"));
    }
}
